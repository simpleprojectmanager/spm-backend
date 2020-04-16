package de.simpleprojectmanager.simpleprojectmanager.webpage;

import org.springframework.http.*;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Optional;

@RestController
public class RESTWebpage {

    @RequestMapping("**")
    public ResponseEntity<byte[]> load(RequestEntity request){

        //Requested path
        String path = request.getUrl().getPath();

        //Checks if the path ends with a /
        if(path.endsWith("/"))
            //Appends the default index html
            path+="index.html";

        //Loads the requested file
        Optional<byte[]> content = this.loadResponseFile(path);

        //Checks if the content could be loaded
        if(content.isPresent()){
            //Creates the header
            HttpHeaders header = new HttpHeaders();

            //Tries to guess the content type
            header.set("content-type", URLConnection.guessContentTypeFromName(path));

            //Exits with 200 (OK) and sends the content
            return new ResponseEntity<byte[]>(content.get(),header, HttpStatus.OK);
        }

        //Tries to load the 404 page
        Optional<byte[]> notfoundPage = this.loadFile(new File("resources/404.html"));

        //Checks if the page got found
        if(notfoundPage.isPresent())
            //Returns the 404 page
            return new ResponseEntity<byte[]>(notfoundPage.get(),HttpStatus.NOT_FOUND);

        //Exits without a body and the page not found
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    /**
     * Tries to load a requested path file
     *
     * @param path the requested path
     * @return optionally the response
     */
    public Optional<byte[]> loadResponseFile(String path){
        //Removes potential risk with directories
        while(path.contains("../"))
            path=path.replace("../","");

        //Returns the loaded file
        return this.loadFile(new File("resources/web",path));
    }

    /**
     * Loads the requested file if it exists
     *
     * @param requested the requested file
     */
    public Optional<byte[]> loadFile(File requested){
        try{
            //Reads the file
            return Optional.of(Files.readAllBytes(requested.toPath()));
        }catch (Exception e){
            //Exits without the page loaded
            return Optional.empty();
        }
    }

}
