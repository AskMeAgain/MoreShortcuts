package io.github.askmeagain.more.shortcuts.introducemock.entities;

public enum MockType {
  mock, spy;

  public String toUpperName() {
    return this == mock ? "Mock" : "Spy";
  }
}