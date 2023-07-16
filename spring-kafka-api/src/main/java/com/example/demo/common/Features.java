package com.example.demo.common;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;

public enum Features implements Feature {

  @Label("new feature")
  NEW_PRODUCT;

}
