package zen.proverbs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zen.proverbs.beans.ZenBean;

import javax.inject.Singleton;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ZenServiceImpl implements ZenService {

    private static final List<ZenBean> ZEN_DATA_ORG = Arrays.asList(
            new ZenBean('a', "Unknown", "If you do not get it from yourself, Where will you go for it?"),
            new ZenBean('a', "Unknown", "Even a good thing isn’t as good as nothing."),
            new ZenBean('a', "Fen-Yang", "When you are deluded and full of doubt, even a thousand books of scripture are not enough. When you have realized understanding, even one word is too much."),
            new ZenBean('a', "Koan", "How do you step from the top of a 100-foot pole?"),
            new ZenBean('a', "Shunryu Suzuki", "The most important point is to accept yourself and stand on your two feet"),
            new ZenBean('a', "Basho", "An autumn night… don’t think your life, didn’t matter."),
            new ZenBean('a', "Unknown", "Better to sit all night than to go to bed with a dragon."),
            new ZenBean('a', "Unknown", "Live every day like your hair was on fire."),
            new ZenBean('a', "Unknown", "If you understand, things are just as they are; if you do not understand, things are just as they are."),
            new ZenBean('a', "Basho", "Before enlightenment, chopping wood and carrying water. After enlightenment, chopping wood and carrying water."),
            new ZenBean('a', "Elie Wiesel", "What does mysticism mean? It means the way to attain knowledge. It’s close to philosophy, except in philosophy you go horizontally while in mysticism you go vertically."),
            new ZenBean('a', "Unknown", "There’s no meaning to a flower unless it blooms."),
            new ZenBean('a', "Alan Watts", "Nothing is exactly as it seems, nor is it otherwise."),
            new ZenBean('a', "Unknown", "The ways to the One are as many as the lives of men."),
            new ZenBean('a', "Unknown", "At first, I saw mountains as mountains and rivers as rivers. Then, I saw mountains were not mountains and rivers were not rivers. Finally, I see mountains again as mountains, and rivers again as rivers."),
            new ZenBean('a', "Unknown", "Where there is great doubt, there will be great awakening; small doubt, small awakening, no doubt, no awakening."),
            new ZenBean('a', "Unknown", "If you’re attached to anything, you surely will go far astray."),
            new ZenBean('a', "Jesus Christ", "Let the dead bury the dead"),
            new ZenBean('a', "Unknown", "One falling leaf is not just one leaf; it means the whole autumn."),
            new ZenBean('a', "Unknown", "Knock on the sky and listen to the sound.")
    );

    private static final Logger LOG = LoggerFactory.getLogger(ZenService.class);
    private static final SecureRandom SRAND = new SecureRandom();

    private final List<ZenBean> ZEN_DATA;

    public ZenServiceImpl() {
        ZEN_DATA = new ArrayList<>();
        ZEN_DATA.addAll(ZEN_DATA_ORG);
    }
    public int getProverbCount() {

        return ZEN_DATA.size();
    }

    public int getDefaultProverbCount() {

        return (int) ZEN_DATA.stream()
                .filter(l -> l.getType() == 'a').count();
    }

    public int getCustomProverbCount() {

        return (int) ZEN_DATA.stream()
                .filter(l -> l.getType() == 'c').count();
    }

    public ZenBean getProverb() {
        final ZenBean aquote = ZEN_DATA.get(SRAND.nextInt(ZEN_DATA.size()));
        LOG.info("Returning proverb {} by {}", aquote.getProverb(), aquote.getAuthor());
        return aquote;
    }

    public List<ZenBean> getProverbs() {
        return ZEN_DATA;
    }

    public List<ZenBean> getDefaultProverbs() {
        return ZEN_DATA.stream()
                .filter(l -> l.getType() == 'a').collect(Collectors.toList());
    }

    public List<ZenBean> getCustomProverbs() {
        return ZEN_DATA.stream()
                .filter(l -> l.getType() == 'c').collect(Collectors.toList());
    }

    public void addProverb(final ZenBean prov) {
        LOG.info("Adding proverb from author: {}", prov.getAuthor());
        prov.setType('c');
        ZEN_DATA.add(prov);
    }

    public void addProverbs(final List<ZenBean> prov) {
        LOG.debug("Adding proverb count: {}", prov.size());
        prov.stream().forEach(p -> addProverb(p));
    }
}
