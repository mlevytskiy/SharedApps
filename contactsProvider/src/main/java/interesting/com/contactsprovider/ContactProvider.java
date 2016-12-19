package interesting.com.contactsprovider;

import android.content.Context;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.PhoneNumber;
import com.github.tamir7.contacts.Query;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 29.11.16.
 */

public class ContactProvider {

    public final static ContactProvider instance = new ContactProvider();

    public void init(Context context, FinishInitListener listener) {
        Contacts.initialize(context);
        Query q = Contacts.getQuery();
        List<Contact> contacts = q.find();
        List<String> phoneNumbers = new ArrayList<>();
        for (Contact contact : contacts) {
            phoneNumbers.addAll( getPhones(contact.getPhoneNumbers()) );
        }
        listener.setAll(phoneNumbers);
    }

    private List<String> getPhones(List<PhoneNumber> phoneNumbers) {
        List<String> result = new ArrayList<>();

        for (PhoneNumber pn : phoneNumbers) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto;
            try {
                numberProto = phoneUtil.parse(pn.getNormalizedNumber(), "");
                result.add(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
            } catch (NumberParseException e) {
                try {
                    numberProto = phoneUtil.parse(pn.getNormalizedNumber(), "UA");
                    result.add(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
                } catch (NumberParseException e1) {
                    //do nothing
                }
            }
        }
        return result;
    }

}
