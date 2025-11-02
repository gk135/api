package utils;

import io.restassured.response.Response;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {
    
    public static List<User> processUsersWithOddIds(Response response) {
        List<Integer> userIds = response.jsonPath().getList("data.id", Integer.class);
        List<String> firstNames = response.jsonPath().getList("data.first_name", String.class);
        List<String> lastNames = response.jsonPath().getList("data.last_name", String.class);
        List<String> emails = response.jsonPath().getList("data.email", String.class);
        
        List<User> oddUsers = new ArrayList<>();
        
        for (int i = 0; i < userIds.size(); i++) {
            int id = userIds.get(i);
            if (isIdOdd(id)) {
                User user = new User(id, firstNames.get(i), lastNames.get(i), emails.get(i));
                oddUsers.add(user);
            }
        }
        
        return oddUsers;
    }
    
    public static boolean isIdOdd(int number) {
        return number % 2 != 0;
    }
}
