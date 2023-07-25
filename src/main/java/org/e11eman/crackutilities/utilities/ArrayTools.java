package org.e11eman.crackutilities.utilities;
@SuppressWarnings("unused")
public class ArrayTools {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static <A> ArrayList<A> shift(ArrayList<A> array, int shiftInt) {
        return new ArrayList<>(array.subList(shiftInt, array.size()));
    }

    public static <A> String join(ArrayList<A> array, char joinChar) {
        StringBuilder str = new StringBuilder();

        for(A i : array) {
            str.append(i).append(joinChar);
        }

        return str.toString().trim();
    }

    public static <A> A getRandomInArray(ArrayList<A> array) {
        return array.get(SECURE_RANDOM.nextInt(array.size() - 1));
    }

    @SuppressWarnings("unchecked")
    public static <A> A getRandomInCollection(Collection<A> collection) {
        return (A) collection.toArray()[SECURE_RANDOM.nextInt(collection.size() - 1)];
    }
}
