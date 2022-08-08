package server;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/{file}")
public class PublicController {

    /**
     * Gets a file from the 'public' folder
     * 
     * @param fileName the name of the file to find
     * @return a {@link ResponseEntity<Resource>} containing a 
     * {@code byte[]} with data of the given file 
     */
    @GetMapping("/")
    public ResponseEntity<Resource> getFile(@PathVariable("file") String fileName) {
        File file = new File("server/src/main/resources/public/" + fileName);
        
        if (!fileExists(file)) {
            return ResponseEntity.notFound().build();
        }
        
        Path path = Path.of(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
            resource = ByteArrayResourceFromPath(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    /**
     * Gets a nested file using {@code getFile()}
     * 
     * @param fileName the name of the folder to search in
     * @param fileNameExtended the name of the file to find
     * @return a {@link ResponseEntity<Resource>} containing a 
     * {@code byte[]} with data of the given file 
     */
    @GetMapping("/{fileInFolder}")
    public ResponseEntity<Resource> getFileInFolder(
            @PathVariable("file") String fileName, 
            @PathVariable("fileInFolder") String fileNameExtended) {
        return getFile(fileName + "/" + fileNameExtended);
    }

    /**
     * Adds or overwrites a file on the server
     * 
     * @param fileName the name + path of the file to add/overwrite
     * @param newFile a {@code byte[]} containing the data of the file
     * @return the name of the file
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @PostMapping("/")
    public ResponseEntity<String> addFile(
            @PathVariable("file") String fileName, @RequestBody byte[] newFile) {
        File file = new File("server/src/main/resources/public/" + fileName);
        
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(newFile);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        
        return ResponseEntity.ok(file.getName());
    }
    
    @PostMapping("/{fileInFolder}")
    public ResponseEntity<String> addFileInFolder(
            @PathVariable("file") String fileName, 
            @PathVariable("fileInFolder") String fileNameExtended, 
            @RequestBody byte[] newFile) {
        return addFile(fileName + "/" + fileNameExtended, newFile);
    }

    /**
     * Checks if a file exists
     * 
     * @param file The {@link File} to check
     * @return {@code true} if the {@link File} exists, {@code false} if not 
     */
    protected boolean fileExists (File file) {
        return file.exists();
    }

    /**
     * Reads all bytes from a given path
     * 
     * @param path the path to read from
     * @return all read {@code bytes}
     * @throws IOException if the {@link Path} does not contain a readable file
     */
    protected ByteArrayResource ByteArrayResourceFromPath (Path path) throws IOException {
        return new ByteArrayResource(Files.readAllBytes(path));
    }
}
