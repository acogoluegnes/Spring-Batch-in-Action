/**
 * Copyright (c) 2010 FullSIX
 *
 * $Id: Customer.java 229 2011-04-13 06:32:27Z acogoluegnes $
 */
package com.manning.sbia.ch06.mail;

/**
 * @author bazoud
 */
public class Customer {
  private String id;
  private String name;
  private String email;

  public Customer() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
