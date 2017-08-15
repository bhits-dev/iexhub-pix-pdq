package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.service.PixOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pix/persons")
public class PixOperationController {
    @Autowired
    PixOperationService pixOperationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerPerson(@RequestBody String reqXMLPath){
        String pixAddMessage = pixOperationService.addPerson(reqXMLPath);
        return pixAddMessage;
    }


}
