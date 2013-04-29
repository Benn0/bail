package bail



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(NormalizerService)
class NormalizerServiceTests {

    public void testNormalizeEmailAddress() {
        String out = service.normalizeEmailAddress(" Hans@FFdd.ch ");
        assertEquals("hans@ffdd.ch", out);
    }

    public void testNormalizeSubject() throws Exception {
        String out = service.normalizeSubject(" Hello ");
        assertEquals("Hello", out);

        out = service.normalizeSubject(" Re:  Schöner  Tag! Re:nate ");
        assertEquals("Schöner  Tag! Re:nate", out);

        out = service.normalizeSubject("Re:  AW: FWD:RE: Schöner  Tag! Re:nate ");
        assertEquals("Schöner  Tag! Re:nate", out);

        out = service.normalizeSubject(null);
        assertEquals("", out);
    }

}
