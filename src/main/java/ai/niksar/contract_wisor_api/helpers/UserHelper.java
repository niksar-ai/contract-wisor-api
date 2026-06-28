package ai.niksar.contract_wisor_api.helpers;

import ai.niksar.contract_wisor_api.exception.ContractWisorException;

public class UserHelper {

    public static String extractNameTitle(String name,String surname) {
        return capitalize(name) + " " + surname.toUpperCase();
    }
    public static String extractUserName(String email) {
        String userName = email.split("@")[0];
        if (!userName.isEmpty()) {
            return userName;
        } else {
            throw new ContractWisorException.E017();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
