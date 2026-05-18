package core.controllers.support;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

public final class ComboOptionParser {

    public static final String SELECT_ONE = "Select one";

    private ComboOptionParser() {
    }

    public static boolean isMissingSelection(String value) {
        if (value == null) {
            return true;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return true;
        }
        return SELECT_ONE.equals(trimmed);
    }

    public static Long parseDoctorIdFromCombo(String comboSelection) {
        if (isMissingSelection(comboSelection)) {
            return null;
        }
        String trimmed = comboSelection.trim();
        int separatorIndex = trimmed.indexOf(" - ");
        if (separatorIndex > 0) {
            try {
                return Long.parseLong(trimmed.substring(0, separatorIndex).trim());
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        try {
            return Long.parseLong(trimmed);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
