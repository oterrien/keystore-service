package com.ote.keystore.credential.restcontroller;

import com.ote.keystore.credential.mapper.CredentialMapperService;
import com.ote.keystore.credential.payload.CredentialPayload;
import com.ote.keystore.credential.persistence.CredentialEntity;
import com.ote.keystore.credential.persistence.CredentialPersistenceService;
import com.ote.keystore.page.Page;
import com.ote.keystore.page.PageMapperService;
import com.ote.keystore.trace.annotation.Traceable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PropertySource("${credential.config.location:classpath:credential.yml}")
@RestController
@RequestMapping("/v1/keys/credentials")
@Slf4j
@Validated
public class CredentialRestController {

    @Autowired
    private CredentialPersistenceService credentialPersistenceService;

    @Autowired
    private CredentialMapperService credentialMapperService;

    @Autowired
    private PageMapperService pageMapperService;

    // region UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.INFO)
    public CredentialPayload read(@PathVariable("id") Integer id,
                                  @RequestHeader String secretKey) {
        log.trace("get credential where id " + id);
        return credentialMapperService.convert(credentialPersistenceService.find(id, secretKey));
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public Page<CredentialPayload> read(@ModelAttribute CredentialPayload payloadFilter,
                                        @RequestParam(required = false) String sortingBy,
                                        @RequestParam(required = false, defaultValue = "ASC") String sortingDirection,
                                        @RequestParam(required = false, defaultValue = "${page.default.size}") int pageSize,
                                        @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                        @RequestHeader String secretKey) {
        log.trace("get credentials where filter is " + payloadFilter);
        Specification<CredentialEntity> filter = credentialMapperService.getFilter(payloadFilter, secretKey);
        Pageable pageRequest = credentialMapperService.getPageRequest(sortingBy, sortingDirection, pageSize, pageIndex);
        return pageMapperService.convertTo(credentialPersistenceService.find(filter, pageRequest).map(p -> credentialMapperService.convert(p)));
    }
    // endregion

    // region UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public CredentialPayload reset(@PathVariable("id") Integer id,
                                   @Valid @RequestBody CredentialPayload payload,
                                   @RequestHeader String secretKey) {
        log.trace("update credential where id " + id);
        return credentialMapperService.convert(credentialPersistenceService.reset(id, credentialMapperService.convert(payload), secretKey));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public CredentialPayload patch(@PathVariable("id") Integer id,
                                   @Valid @RequestBody CredentialPayload payload,
                                   @RequestHeader String secretKey) {
        log.trace("update credential where id " + id);
        return credentialMapperService.convert(credentialPersistenceService.merge(id, credentialMapperService.convert(payload), secretKey));
    }
    // endregion

    // region CREATE
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public CredentialPayload create(@Valid @RequestBody CredentialPayload payload,
                                    @RequestHeader String secretKey) {
        log.trace("create credential");
        return credentialMapperService.convert(credentialPersistenceService.create(credentialMapperService.convert(payload), secretKey));
    }
    // endregion

    // region DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Traceable(level = Traceable.Level.TRACE)
    public void delete(@PathVariable("id") int id) {
        log.trace("delete credential where id " + id);
        credentialPersistenceService.delete(id);
    }


    @RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Traceable(level = Traceable.Level.TRACE)
    public void delete(@ModelAttribute CredentialPayload payloadFilter,
                       @RequestHeader String secretKey) {

        log.trace("remove credentials where filter is " + payloadFilter);
        Specification<CredentialEntity> filter = credentialMapperService.getFilter(payloadFilter, secretKey);
        credentialPersistenceService.delete(filter);
    }
    // endregion
}
