package edu.kmaooad;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class FunctionTest {

    private static final String BAD_RESPONSE = "Request body must contains an update json";
    private static final String SUCC = "Successfully inserted";

    private HttpRequestMessage<Optional<String>> getRequest(Optional<String> queryBody) {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> request = mock(HttpRequestMessage.class);

        doReturn(queryBody).when(request).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(request).createResponseBuilder(any(HttpStatus.class));

        return request;
    }

    private ExecutionContext getContext() {
        // Setup
        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();
        return context;
    }

    @Test
    public void testFunctionWithoutBody() {
        // Invoke
        final HttpResponseMessage response = new Function().run(getRequest(Optional.empty()), getContext());
        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(BAD_RESPONSE, response.getBody());
    }

    @Test
    public void testFunctionWithWrongStringInBody(){
        // Invoke
        final HttpResponseMessage response = new Function().run(getRequest(Optional.of("Azure")), getContext());
        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals(BAD_RESPONSE, response.getBody());
    }

    @Test
    public void testFunctionWithWrongTypeOfMessageIdInBody(){
        // Invoke
        final HttpResponseMessage ret = new Function().run(getRequest(Optional.of("{\n" +
                "    \"message\": {\n" +
                "        \"message_id\": some_id\n" +
                "    }\n" +
                "}")), getContext());
        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, ret.getStatus());
        assertEquals(BAD_RESPONSE, ret.getBody());
    }

    @Test
    public void testFunctionWithCorrectBody(){
        // Invoke
        final HttpResponseMessage ret = new Function().run(getRequest(Optional.of("{\n" +
                "    \"message\": {\n" +
                "        \"message_id\": 12345\n" +
                "    }\n" +
                "}")), getContext());
        // Verify
        assertEquals(HttpStatus.OK, ret.getStatus());
        assertEquals(SUCC, ret.getBody());
    }

}
