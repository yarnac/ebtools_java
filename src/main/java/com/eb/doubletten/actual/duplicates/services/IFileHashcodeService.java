package com.eb.doubletten.actual.duplicates.services;

import java.nio.file.Path;

public interface IFileHashcodeService {

    public String getHashCode(Path file);
}

