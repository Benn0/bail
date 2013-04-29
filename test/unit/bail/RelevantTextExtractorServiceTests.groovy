package bail



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(RelevantTextExtractorService)
class RelevantTextExtractorServiceTests {

    void test_isBeginningOfNoise() {
        assertTrue service.isBeginningOfNoise("Am 14.01.2011 um 21:49 schrieb David Benninger:")
        assertFalse service.isBeginningOfNoise("Am14.01.2011um21:49schriebDavid Benninger:")

        assertTrue service.isBeginningOfNoise("On 16/03/2011, at 7:18 AM, David Benninger wrote:")
    }
}
