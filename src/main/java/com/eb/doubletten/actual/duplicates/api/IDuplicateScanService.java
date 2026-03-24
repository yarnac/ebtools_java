package com.eb.doubletten.actual.duplicates.api;

import java.nio.file.Path;
import java.util.List;

public interface IDuplicateScanService {
    List<? extends IDuplikatContainer> scan(Path root);
}
