package zen.proverbs.controller;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import zen.proverbs.beans.ResponseBean;
import zen.proverbs.beans.ZenBean;
import zen.proverbs.beans.ZenBeans;
import zen.proverbs.service.ZenServiceImpl;

import javax.inject.Inject;
import java.util.List;

@Controller("/zen")
@Requires(beans = ZenServiceImpl.class)
public class ZenController {

    @Inject
    private ZenServiceImpl zens;

    @Get("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public int getProverbCount() {
        return zens.getProverbCount();
    }

    @Get("/count/default")
    @Produces(MediaType.TEXT_PLAIN)
    public int getDefaultProverbCount() {
        return zens.getDefaultProverbCount();
    }

    @Get("/count/custom")
    @Produces(MediaType.TEXT_PLAIN)
    public int getCustomProverbCount() {
        return zens.getCustomProverbCount();
    }

    @Get("/proverb")
    @Produces(MediaType.APPLICATION_JSON)
    public ZenBean getProverb() {
        return zens.getProverb();
    }

    @Get("/proverbs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ZenBean> getProverbs() {
        return zens.getProverbs();
    }

    @Get("/proverbs/default")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ZenBean> getProverbsDefault() {
        return zens.getDefaultProverbs();
    }

    @Get("/proverbs/custom")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ZenBean> getProverbsCustom() {
        return zens.getCustomProverbs();
    }

    @Post("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseBean addProverb(final ZenBean zen) {
        zens.addProverb(zen);

        final ResponseBean resp =
                new ResponseBean(0, "Added one proverb with author: " + zen.getAuthor());
        return resp;
    }

    @Post("/addmulti")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseBean addProverbs(final ZenBeans zen) {
        zens.addProverbs(zen.getZens());

        final ResponseBean resp =
                new ResponseBean(0, "Added proverb count: " + zen.getZens().size());
        return resp;
    }
}
