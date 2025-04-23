package modal;

public enum SportType {
    FUTBOL,
    BASKET_VOLEYBOL,
    MASA_TENISI,
    TENIS_CALISMA_DUVARI,
    TENIS,
    STAD_KIRALAMA;

    public static SportType fromString(String s) {
        return switch (s.toUpperCase()) {
            case "FUTBOL" -> FUTBOL;
            case "BASKET-VOLEYBOL" -> BASKET_VOLEYBOL;
            case "MASA TENİSİ" -> MASA_TENISI;
            case "TENİS ÇALIŞMA DUVARI" -> TENIS_CALISMA_DUVARI;
            case "TENİS" -> TENIS;
            case "STAD KİRALAMA" -> STAD_KIRALAMA;
            default -> throw new IllegalArgumentException("Unknown sport type: " + s);
        };
    }
}