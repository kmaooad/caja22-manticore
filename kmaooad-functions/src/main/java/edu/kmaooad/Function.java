package edu.kmaooad;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.Optional;

import org.bson.Document;
import org.bson.json.JsonParseException;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/TelegramWebhook". To invoke it using "curl" command in bash:
     * curl -d "HTTP Body" {your host}/api/TelegramWebhook
     */
    @FunctionName("TelegramWebhook")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.FUNCTION)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        try {
            final String requestBodyString = request.getBody().orElseThrow(EmptyRequestBodyException::new);

            String connectionStringUri = "mongodb+srv://manticore:SaYvqqSw7MYjW1v5@manticore-db.9wxedmu.mongodb.net/?retryWrites=true&w=majority";
            try (MongoClient mongoClient = MongoClients.create(connectionStringUri)) {
                MongoDatabase database = mongoClient.getDatabase("manticore-db");
                MongoCollection<Document> collection = database.getCollection("updates");
                Document doc = Document.parse(requestBodyString);
                collection.insertOne(doc).toString();
                return request.createResponseBuilder(HttpStatus.OK).body("Successfully inserted").build();
            } catch (JsonParseException e) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Request body must contains an update json").build();
            }
        } catch (EmptyRequestBodyException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Request body must contains an update json").build();
        }
    }
}