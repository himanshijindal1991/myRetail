package com.target.myRetail.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.target.myRetail.errors.MyRetailException;
import com.target.myRetail.model.Product;
import com.target.myRetail.model.Current_price;
import com.target.myRetail.model.ProductWithProductPrice;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;


import java.time.Duration;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Service
public class MyRetailService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private MongoClient mongoClient;

    private String DOWNSTREAM_URI = System.getenv().get("DOWNSTREAM_URI");


    private boolean is5xxServerError(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }

    public Optional<ProductWithProductPrice> getProduct(String productId) throws MyRetailException {
        Product product;
        Document document;
        Decimal128 value;
        try {
            // 1. call the downstream service, if downstream service fails retry the call 3 times in duration of 100 ms
            // 2. set the timeout of 0.5 seconds to handle latency
            product = webClientBuilder.build().get()
                    .uri(DOWNSTREAM_URI + "/" + productId)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new MyRetailException("4xx error! while calling downstream")))
                    .bodyToMono(Product.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100))
                            .filter(this::is5xxServerError))
                    .timeout(Duration.ofMillis(500))
                    .block();
        } catch (final Exception e) {
            throw new MyRetailException("Something went wrong while calling downstream");
        }

        try {
            // One enhancement, instead of calling DB, we could have cache like Redis or memcache
            // to get the productDetails from cache instead of hitting the DB every time
            final MongoDatabase database = mongoClient.getDatabase("test");
            final MongoCollection<Document> collection = database.getCollection("productPrice");
            document = collection.find(eq("_id", productId)).first();
            value = (Decimal128) document.get("value");
        } catch (final Exception e) {
            throw new MyRetailException("Something went wrong while retrieving data from Mongo DB");
        }

        final String currency_code = (String) document.get("currency_code");
        final Current_price current_price = new Current_price(value.doubleValue(), currency_code);
        if (product != null && product.getId() != null && product.getName() != null && current_price != null) {
            return Optional.of(
                    new ProductWithProductPrice(product.getId(), product.getName(), current_price));
        }
        return Optional.empty();
    }


}
