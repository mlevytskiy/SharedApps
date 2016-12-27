package interesting.com.contactsprovider;

import android.content.Context;
import android.text.TextUtils;

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

    public static final PhoneNumberUtil.PhoneNumberFormat PHONE_FORMAT = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;
    public final static ContactProvider instance = new ContactProvider();

    public void init(Context context, String countryCode, String myPhoneNumber, FinishInitListener listener) {
        Contacts.initialize(context);
        Query q = Contacts.getQuery();
        List<Contact> contacts = q.find();
        List<String> phoneNumbers = new ArrayList<>();
        for (Contact contact : contacts) {
            phoneNumbers.addAll( getPhones(contact.getPhoneNumbers(), "", countryCode) );
        }
        listener.setAll(phoneNumbers);
    }

    private List<String> getPhones(List<PhoneNumber> phoneNumbers, String myPhoneNumber, String countryCode) {
        List<String> result = new ArrayList<>();

        for (PhoneNumber pn : phoneNumbers) {
            String phoneNumber = getPhoneNumber(pn.getNormalizedNumber(), countryCode);
            if ( !TextUtils.isEmpty(phoneNumber) && !TextUtils.equals(myPhoneNumber, phoneNumber) ) {
                result.add(phoneNumber);
            } else {
                //do nothing
            }
        }

        return result;
    }

    public String getPhoneNumber(String phoneNumber, String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto;
        try {
            numberProto = phoneUtil.parse(phoneNumber, "");
            return phoneUtil.format(numberProto, PHONE_FORMAT);
        } catch (NumberParseException e) {
            try {
                numberProto = phoneUtil.parse(phoneNumber, countryCode);
                return phoneUtil.format(numberProto, PHONE_FORMAT);
            } catch (NumberParseException e1) {
                return null;
            }
        }
    }

}
