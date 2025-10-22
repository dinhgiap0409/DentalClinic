package utils;

public class Validation {

    /*
    made by Vu Quang Hieu
     */
    private Validation() {
    }

//    public static void checkNumInt(String input, int min, int max) throws ProgramException {
//        try {
//            int number = Integer.parseInt(input.trim());
//            if (number >= min && number <= max) {
//                return;
//            }
//            throw new ProgramException(String.format(Message.ERROR_INPUTNUMBER_MSG, min, max));
//        } catch (NumberFormatException e) {
//            throw new ProgramException(Message.ERROR_INVALID_MSG);
//        }
//    }
//
//    public static void checkStringString(String messageInfo) throws ProgramException {
//        if (messageInfo.isEmpty()) {
//            throw new ProgramException(Message.ERROR_INVALID_MSG);
//        }
//    }
    
    public static void checkPhoneNumber(String input) throws ProgramException {
        if (input.isEmpty()) {
            throw new ProgramException("Số điện thoại không được để trống!");
        }
        if (!input.matches("\\d{10}$")) {
            throw new ProgramException("Số điện thoại phải là chuỗi có 10 số và không được có chữ!");
        }
    }
}
