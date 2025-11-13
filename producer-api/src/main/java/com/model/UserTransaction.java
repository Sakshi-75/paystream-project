package com.model;

public record UserTransaction (Integer user_id,
                               double amount,
                               String currency){

}
