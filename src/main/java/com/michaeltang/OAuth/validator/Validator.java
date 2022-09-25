package com.michaeltang.OAuth.validator;

import java.security.Principal;
import java.util.Map;

public interface Validator {
    void validate(Principal principal, Map<String, String> parameters) throws Exception;
}
