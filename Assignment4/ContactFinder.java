import java.util.ArrayList;
import java.util.HashSet;

public class ContactFinder {
    // todo for students
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n^2) where n is the number of circles.
    public static HashSet<ContactResult> getContactsNaive(ArrayList<Circle> circles) {
        HashSet <ContactResult> contactedCircles = new HashSet<ContactResult>();
        for (Circle c : circles){
            for (Circle d : circles){
                if (c != d) {
                    ContactResult possibleContact = c.isContacting(d);
                    if (possibleContact != null) {
                        contactedCircles.add(possibleContact);
                    }
                }
            }
        }
        return contactedCircles;
    }

    // todo for students
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n*log(n)) where n is the number of circles.
    public static HashSet<ContactResult> getContactsBVH(ArrayList<Circle> circles, BVH bvh) {
        HashSet<ContactResult> allContacts = new HashSet<>();
        //ContactResult newContact = getContactBVH();
        for (Circle e : circles){
            HashSet<ContactResult> contactHash = getContactBVH(e,bvh);
            allContacts.addAll(contactHash);

        }

        return allContacts;
    }

    // todo for students
    // Takes a single circle c and a BVH bvh.
    // Returns a HashSet of ContactResult objects representing contacts between c
    // and the circles contained in the leaves of the bvh.
    public static HashSet<ContactResult> getContactBVH(Circle c, BVH bvh) {
        HashSet<ContactResult> ourHashSet = new HashSet<>();

        if (!(c.getBoundingBox().intersectBox(bvh.boundingBox)) || (c.equals(bvh.containedCircle))){

            return ourHashSet;
        } else if (bvh.containedCircle != null){
            if (c.isContacting(bvh.containedCircle) != null){
                ourHashSet.add(c.isContacting(bvh.containedCircle));
                return ourHashSet;
            }
        }

        if (bvh.child1 != null ){
            HashSet<ContactResult> possibleContact1 = getContactBVH(c,bvh.child1);
            ourHashSet.addAll(possibleContact1);
            //return ourHashSet;

        }
        if (bvh.child2 != null ){
            HashSet<ContactResult> possibleContact2 = getContactBVH(c,bvh.child2);
            ourHashSet.addAll(possibleContact2);


        }
        return ourHashSet;
    }
}
