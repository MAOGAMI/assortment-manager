package pl.daro.assortmentmanager.photos;

import com.opencsv.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


import static com.opencsv.ICSVWriter.*;
@Controller
public class PhotosLister {
    String[] arrayOfParts;
    List<String[]> listOfFilesAsStrings = new ArrayList<>();

    @GetMapping("/list")
    @ResponseBody
    public List<File> listFilesFromDirectory(@RequestParam String rootDirectoryPath) {
        File rootDirectory = new File(rootDirectoryPath);
        File[] fList = rootDirectory.listFiles();
        List<File> fileList = new ArrayList<>(Arrays.asList(fList));
        for (File f : fList) {
            if(f.isDirectory()){
                fileList.addAll(listFilesFromDirectory(f.getAbsolutePath()));
            }
            String fileToString = f.toString();
            arrayOfParts = fileToString.split("\\\\");
            listOfFilesAsStrings.add(arrayOfParts);
        }
        return fileList;
    }

    @GetMapping("/write")
    @ResponseBody
    void writeToCsv(){
        try {
            Writer writer = new FileWriter("./csv.csv");
            CSVWriter csvWriter = new CSVWriter(writer, ';', NO_QUOTE_CHARACTER);
            csvWriter.writeAll(listOfFilesAsStrings);
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("done creating csv");
    }

//    public static void main(String[] args) {
//        PhotosLister photosLister = new PhotosLister();
//        photosLister.listFilesFromDirectory("O:/zdjęcia produktów");
//        photosLister.writeToCsv();
//    }

}
