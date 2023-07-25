package org.e11eman.crackutilities.utilities;
@SuppressWarnings("unused")
public class MessagePresets {
    public static final Text errorPrefix = Text.literal("[").formatted(Formatting.DARK_GRAY)
            .append(Text.literal("OMITTED").formatted(Formatting.RED))
            .append(Text.literal("]").formatted(Formatting.DARK_GRAY));

    public static final Text normalPrefix = Text.literal("[").formatted(Formatting.DARK_GRAY)
            .append(Text.literal("OMITTED").formatted(Formatting.BLUE))
            .append(Text.literal("]").formatted(Formatting.DARK_GRAY));

    public static Text errorTextPreset(String errorMessage) {
        return errorPrefix.copy().append(Text.literal(": ").formatted(Formatting.GRAY))
                .append(Text.literal(errorMessage).formatted(Formatting.RED));
    }

    public static Text normalTextPreset(String message) {
        return normalPrefix.copy().append(Text.literal(": ").formatted(Formatting.GRAY))
                .append(Text.literal(message).formatted(Formatting.BLUE));
    }

    public static Text trueTextPreset(String message) {
        return normalPrefix.copy().append(Text.literal(": ").formatted(Formatting.GRAY))
                .append(Text.literal(message).formatted(Formatting.GREEN));
    }

    public static Text falseTextPreset(String message) {
        return normalPrefix.copy().append(Text.literal(": ").formatted(Formatting.GRAY))
                .append(Text.literal(message).formatted(Formatting.RED));
    }
}
