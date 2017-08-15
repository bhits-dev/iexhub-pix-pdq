package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.service.PixOperationService;
import gov.samhsa.c2s.pixclient.util.PixManagerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String revisePerson(@RequestBody String reqXMLPath){
        String pixReviseMessage = pixOperationService.updatePerson(reqXMLPath);
        return pixReviseMessage;
    }

    @PostMapping("/eid")
    @ResponseStatus(HttpStatus.OK)
    public String getPersonEid(@RequestBody String reqXMLPath){
        String eid = pixOperationService.getPersonEid(reqXMLPath);
        return eid;
    }
}
