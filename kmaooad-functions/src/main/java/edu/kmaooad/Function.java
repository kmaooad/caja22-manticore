package edu.kmaooad;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

import org.json.JSONObject;

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

        try
        {
            final String RequestBody = request.getBody().orElse(null);
            if (RequestBody != null)
            {
                JSONObject JSONBody = new JSONObject(RequestBody);
                final int MessageID = JSONBody.getJSONObject("message").getInt("message_id");

                return request.createResponseBuilder(HttpStatus.OK).body("200 OK " + MessageID).build();
            }
            else
            {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Empty Body received.").build();
            }
        }
        catch (Exception e)
        {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("BAD REQUEST").build();
        }
    }
}
