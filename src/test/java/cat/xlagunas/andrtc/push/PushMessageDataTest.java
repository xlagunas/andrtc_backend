package cat.xlagunas.andrtc.push;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;

import cat.xlagunas.andrtc.common.MessageType;

public class PushMessageDataTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void whenCreatedThroughBuilder_thenObjectHasRightValues() {
        PushMessageData message = getTestMessage();

        assertThat(message.getEventType()).isEqualTo(MessageType.ACCEPT_CALL);
        assertThat(message.getParams()).hasSize(2);
        assertThat(message.getParams()).extracting("firstParam").contains(15).hasSize(1);
        assertThat(message.getParams()).extracting("secondParam").contains("anotherValue").hasSize(1);
    }

    @Test
    public void whenSerialized_thenAllPropertiesFound() throws IOException {
        Writer writer = new StringWriter();
        PushMessageData testMessage = getTestMessage();
        mapper.writeValue(writer, testMessage);

        System.out.println(writer.toString());

        PushMessageData message = mapper.readValue(writer.toString(), PushMessageData.class);
        assertThat(message).isEqualToComparingFieldByField(testMessage);
    }

    private PushMessageData getTestMessage() {
        return new PushMessageData(MessageType.ACCEPT_CALL,
                ImmutableMap.<String, Object>builder()
                        .put("firstParam", 15)
                        .put("secondParam", "anotherValue")
                        .build());

    }

}