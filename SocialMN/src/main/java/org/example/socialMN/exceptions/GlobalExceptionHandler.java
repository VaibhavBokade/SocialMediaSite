//package org.example.socialMN.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends Exception{
//
//    @ExceptionHandler(value = UserDataRetrievalException.class)
//    public ResponseEntity<?> handleUserDataRetrievalException() {
////        model.addAttribute("msg", "UserDataRetrieval Exception has occurred");
//        return new ResponseEntity<>("Error retrieving user data", HttpStatus.NOT_FOUND);
//    }
//
//
//
//
//
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = AddFriendException.class)
//    public String handleAddFriendException(Model model) {
//        model.addAttribute("msg", "AddFriendException has occurred");
//        return "error";
//    }
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    @ExceptionHandler(value = AreFriendsException.class)
//    public String handleAreFriendsException(Model model) {
//        model.addAttribute("msg", "AreFriendsException has occurred");
//        return "error";
//    }
//
//
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    @ExceptionHandler(value = SuggestedFriendsException.class)
//    public String handleSuggestedFriendsException(Model model) {
//        model.addAttribute("msg", "SuggestedFriendsException has occurred");
//        return "error";
//    }
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    @ExceptionHandler(value = RemoveFriendException.class)
//    public String handleRemoveFriendException(Model model) {
//        model.addAttribute("msg", "RemoveFriendException has occurred");
//        return "error";
//    }
//
//
//
//
//
//}
