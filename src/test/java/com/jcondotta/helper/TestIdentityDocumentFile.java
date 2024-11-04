package com.jcondotta.helper;

public enum TestIdentityDocumentFile {
    GIF("identity-document.gif"),
    PNG("identity-document.png"),
    JPEG("identity-document.jpeg");

    private final String fileName;

    TestIdentityDocumentFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
