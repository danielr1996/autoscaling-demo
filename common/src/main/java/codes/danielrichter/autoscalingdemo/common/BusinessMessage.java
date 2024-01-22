package codes.danielrichter.autoscalingdemo.common;


import java.io.Serializable;

public record BusinessMessage(int id, String message) implements Serializable {
}
