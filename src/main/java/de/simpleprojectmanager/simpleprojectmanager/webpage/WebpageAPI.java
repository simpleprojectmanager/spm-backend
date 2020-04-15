package de.simpleprojectmanager.simpleprojectmanager.webpage;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Optional;

@RestController
public class WebpageAPI {

    @RequestMapping("**")
    public ResponseEntity load(RequestEntity request){

        //Requested path
        String path = request.getUrl().getPath();

        //Checks if the path ends with a /
        if(path.endsWith("/"))
            //Appends the default index html
            path+="index.html";

        //Loads the requested file
        Optional<String> content = this.loadResponseFile(path);

        //Checks if the content could be loaded
        if(content.isPresent())
            //Exits with 200 (OK) and sends the content
            return new ResponseEntity<String>(content.get(), HttpStatus.OK);

        //Tries to load the 404 page
        Optional<String> notfoundPage = this.loadFile(new File("resources/404.html"));

        //Checks if the page got found
        if(notfoundPage.isPresent())
            //Returns the 404 page
            return new ResponseEntity<String>(notfoundPage.get(),HttpStatus.NOT_FOUND);

        //Exits without a body and the page not found
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    /**
     * Tries to load a requested path file
     *
     * @param path the requested path
     * @return optionally the response
     */
    public Optional<String> loadResponseFile(String path){
        //Removes potential risk with directorys
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
    public Optional<String> loadFile(File requested){
        try(BufferedReader br = new BufferedReader(new FileReader(requested))){
            //Content
            String content="";
            //Placeholder
            String ph;

            //Loads the file content
            while((ph=br.readLine())!=null)
                content+=ph;

            //Returns the content
            return Optional.of(content);
        }catch (Exception e){
            //Exits without the page loaded
            return Optional.empty();
        }
    }

}
