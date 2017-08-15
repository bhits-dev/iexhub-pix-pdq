package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.service.PixOperationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pix/persons")
public class PixOperationController {


    private final PixOperationService pixOperationService;

    public PixOperationController(PixOperationService pixOperationService) {
        this.pixOperationService = pixOperationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerPerson(@RequestBody String reqXMLPath) {
        return pixOperationService.addPerson(reqXMLPath);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String revisePerson(@RequestBody String reqXMLPath) {
        return pixOperationService.updatePerson(reqXMLPath);
    }

    @PostMapping("/eid")
    @ResponseStatus(HttpStatus.OK)
    public String getPersonEid(@RequestBody String reqXMLPath) {
        return pixOperationService.getPersonEid(reqXMLPath);
    }
}
