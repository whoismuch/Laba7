package common.exceptions;

import java.io.FileNotFoundException;

public class NoPermissionsException extends FileNotFoundException {
    public NoPermissionsException(String s) {
        super(s);
    }
}
