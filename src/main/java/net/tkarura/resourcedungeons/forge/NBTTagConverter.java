package net.tkarura.resourcedungeons.forge;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTBase;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagByte;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagByteArray;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagCompound;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagDouble;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagEnd;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagFloat;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagInt;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagIntArray;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagList;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagLong;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagShort;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagString;

public final class NBTTagConverter {

    private NBTTagConverter() {
    }

    public static DNBTBase convert(NBTBase nbt) {

        if (nbt == null) {
            return new DNBTTagEnd();
        }

        switch (NBTBase.NBT_TYPES[nbt.getId()]) {

            case "BYTE": {
                return new DNBTTagByte(((NBTPrimitive) nbt).getByte());
            }

            case "SHORT": {
                return new DNBTTagShort(((NBTPrimitive) nbt).getShort());
            }

            case "INT": {
                return new DNBTTagInt(((NBTPrimitive) nbt).getInt());
            }

            case "LONG": {
                return new DNBTTagLong(((NBTPrimitive) nbt).getLong());
            }

            case "FLOAT": {
                return new DNBTTagFloat(((NBTPrimitive) nbt).getFloat());
            }

            case "DOUBLE": {
                return new DNBTTagDouble(((NBTPrimitive) nbt).getDouble());
            }

            case "BYTE[]": {
                return new DNBTTagByteArray(((NBTTagByteArray) nbt).getByteArray());
            }

            case "STRING": {
                return new DNBTTagString(((NBTTagString) nbt).getString());
            }

            case "LIST": {
                return convert((NBTTagList) nbt);
            }

            case "COMPOUND": {
                return convert((NBTTagCompound) nbt);
            }

            case "INT[]": {
                return new DNBTTagIntArray(((NBTTagIntArray) nbt).getIntArray());
            }

            case "END": {
                return new DNBTTagEnd();
            }

            default: {
                return new DNBTTagEnd();
            }

        }
    }

    public static NBTBase convert(DNBTBase nbt) {

        if (nbt == null) {
            return new NBTTagEnd();
        }

        switch (nbt.getTypeId()) {

            case DNBTBase.TAG_BYTE: {
                return new NBTTagByte(((DNBTTagByte) nbt).getValue());
            }

            case DNBTBase.TAG_SHORT: {
                return new NBTTagShort(((DNBTTagShort) nbt).getValue());
            }

            case DNBTBase.TAG_INT: {
                return new NBTTagInt(((DNBTTagInt) nbt).getValue());
            }

            case DNBTBase.TAG_LONG: {
                return new NBTTagLong(((DNBTTagLong) nbt).getValue());
            }

            case DNBTBase.TAG_FLOAT: {
                return new NBTTagFloat(((DNBTTagFloat) nbt).getValue());
            }

            case DNBTBase.TAG_DOUBLE: {
                return new NBTTagDouble(((DNBTTagDouble) nbt).getValue());
            }

            case DNBTBase.TAG_BYTE_ARRAY: {
                return new NBTTagByteArray(((DNBTTagByteArray) nbt).getValue());
            }

            case DNBTBase.TAG_STRING: {
                return new NBTTagString(((DNBTTagString) nbt).getValue());
            }

            case DNBTBase.TAG_LIST: {
                return convert((DNBTTagList) nbt);
            }

            case DNBTBase.TAG_COMPOUND: {
                return convert((DNBTTagCompound) nbt);
            }

            case DNBTBase.TAG_INT_ARRAY: {
                return new NBTTagByte(((DNBTTagByte) nbt).getValue());
            }

            case DNBTBase.TAG_END: {
                return new NBTTagEnd();
            }

            default: {
                return new NBTTagEnd();
            }

        }

    }

    public static DNBTTagCompound convert(NBTTagCompound nbt) {

        DNBTTagCompound result = new DNBTTagCompound();

        for (String key : nbt.getKeySet()) {

            result.set(key, convert(nbt.getTag(key)));

        }

        return result;
    }

    public static NBTTagCompound convert(DNBTTagCompound nbt) {

        NBTTagCompound result = new NBTTagCompound();

        for (String key : nbt.getValue().keySet()) {

            result.setTag(key, convert(nbt.getValue().get(key)));

        }

        return result;
    }

    public static DNBTTagList convert(NBTTagList nbt) {

        DNBTTagList result = new DNBTTagList();

        for (int i = 0; i < nbt.tagCount(); i++) {

            result.add(convert(nbt.get(i)));

        }

        return result;
    }

    public static NBTTagList convert(DNBTTagList nbt) {

        NBTTagList result = new NBTTagList();

        for (DNBTBase tag : nbt.getValue()) {

            result.appendTag(convert(tag));

        }

        return result;
    }

}
