package cat.xlagunas.andrtc.repository.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(builder = PushMessageData.Builder.class)
public class PushMessageData {
    public enum MessageType {CREATE_CALL, ACCEPT_CALL, REJECT_CALL, REQUEST_FRIENDSHIP, ACCEPT_FRIENDSHIP, REJECT_FRIENDSHIP}

    public final MessageType eventType;
    public final Map<String, Object> params;

    private PushMessageData(MessageType eventType, Map<String, Object> params) {
        this.eventType = eventType;
        this.params = params;
    }

    public static Builder builder(MessageType eventType) {
        return new Builder(eventType);
    }

    @JsonPOJOBuilder(buildMethodName = "build")
    public static class Builder {
        private MessageType eventType;
        private Map<String, Object> objectMap;

        @JsonCreator
        public Builder(@JsonProperty("eventType") MessageType eventType) {
            this.eventType = eventType;
        }

        @JsonSetter("params")
        public Builder setMap(Map<String, Object> map) {
            this.objectMap = map;
            return this;
        }

        public Builder addParams(String property, Object value) {
            if (objectMap == null) {
                objectMap = new HashMap<>();
            }
            objectMap.put(property, value);
            return this;
        }

        public PushMessageData build() {
            return new PushMessageData(eventType, objectMap);
        }
    }
}