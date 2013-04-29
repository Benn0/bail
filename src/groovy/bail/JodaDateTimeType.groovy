package bail

import com.mongodb.BasicDBObject
import com.mongodb.DBObject
import org.grails.datastore.mapping.engine.types.AbstractMappingAwareCustomTypeMarshaller
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.query.Query
import org.joda.time.DateTime

/**
 * Created with IntelliJ IDEA.
 * User: benno
 * Date: 4/20/13
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */
class JodaDateTimeType extends AbstractMappingAwareCustomTypeMarshaller<DateTime, DBObject, DBObject> {

    public JodaDateTimeType() {
        super(DateTime)
    }

    @Override
    protected Object writeInternal(PersistentProperty property, String key, DateTime value, DBObject nativeTarget) {
        Date converted = value.toDate()
        nativeTarget.put(key, converted)
        return converted
    }

    @Override
    protected DateTime readInternal(PersistentProperty property, String key, DBObject nativeSource) {
        Object object = nativeSource.get(key)
        if(object instanceof Date) {
            return new DateTime((Date)object)
        }
        return null
    }

}
