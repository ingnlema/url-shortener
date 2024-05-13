package com.mercadolibre.urlshortener.resource;

import com.mercadolibre.urlshortener.controller.UrlController;
import com.mercadolibre.urlshortener.model.UrlMapping;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UrlResourceAssembler extends RepresentationModelAssemblerSupport<UrlMapping, UrlResource> {

    public UrlResourceAssembler() {
        super(UrlController.class, UrlResource.class);
    }

    @Override
    public UrlResource toModel(UrlMapping entity) {
        UrlResource resource = new UrlResource(entity);
        resource.add(linkTo(methodOn(UrlController.class).getOriginalUrl(entity.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UrlController.class).deleteShortUrl(entity.getId())).withRel("delete"));
        return resource;
    }
}
