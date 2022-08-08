package server;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PublicControllerTest {

    PublicController sut;

    @Test
    public void FileIsThere() {
        sut = new IPublicController();
        assertArrayEquals(
                new byte[]{0, 0, 1}, 
                ((ByteArrayResource) Objects.requireNonNull(sut.getFile("File").getBody()))
                        .getByteArray()
        );
    }
    
    @Test
    public void FileNotThere() {
        sut = new PublicController();
        assertEquals(ResponseEntity.notFound().build(), sut.getFile("<This is an invalid file>"));
    }
    
}

class IPublicController extends PublicController {
    
    @Override
    protected boolean fileExists (File file) {
        return true;
    }
    
    @Override
    protected ByteArrayResource ByteArrayResourceFromPath (Path path) throws IOException {
        return new ByteArrayResource(new byte[]{0, 0, 1});
    }
}