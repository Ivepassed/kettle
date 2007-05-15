package org.pentaho.di.core.row;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.ibridge.kettle.core.Const;
import be.ibridge.kettle.core.exception.KettleFileException;
import be.ibridge.kettle.core.exception.KettleValueException;

public class RowMeta implements RowMetaInterface
{
    private List valueMetaList;

    public RowMeta()
    {
        valueMetaList = new ArrayList();
    }
    
    public Object clone()
    {
        RowMeta rowMeta = new RowMeta();
        for (int i=0;i<size();i++)
        {
            ValueMetaInterface valueMeta = getValueMeta(i);
            rowMeta.addValueMeta( (ValueMetaInterface)valueMeta.clone() );
        }
        return rowMeta;
    }

    /**
     * @return the list of value Metadata 
     */
    public List getValueMetaList()
    {
        return valueMetaList;
    }

    /**
     * @param valueMetaList the list of valueMeta to set
     */
    public void setValueMeta(List valueMetaList)
    {
        this.valueMetaList = valueMetaList;
    }

    /**
     * @return the number of values in the row
     */
    public int size()
    {
        return valueMetaList.size();
    }

    /**
     * @return true if there are no elements in the row metadata
     */
    public boolean isEmpty()
    {
        return size()==0;
    }

    
    /**
     * Add a metadata value, extends the array if needed.
     * 
     * @param meta The metadata value to add
     */
    public void addValueMeta(ValueMetaInterface meta)
    {
        valueMetaList.add(meta);
    }
    
    /**
     * Get the value metadata on the specified index.
     * @param index The index to get the value metadata from
     * @return The value metadata specified by the index.
     */
    public ValueMetaInterface getValueMeta(int index)
    {
        return (ValueMetaInterface) valueMetaList.get(index);
    }
    
    /**
     * Get a String value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The string found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public String getString(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getString(dataRow[index]);
    }
    
    /**
     * Get an Integer value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The integer found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public Long getInteger(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getInteger(dataRow[index]);
    }

    /**
     * Get a Number value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The number found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public Double getNumber(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getNumber(dataRow[index]);
    }

    /**
     * Get a Date value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The date found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public Date getDate(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getDate(dataRow[index]);
    }

    /**
     * Get a BigNumber value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The bignumber found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public BigDecimal getBigNumber(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getBigNumber(dataRow[index]);
    }

    /**
     * Get a Boolean value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The boolean found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public Boolean getBoolean(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getBoolean(dataRow[index]);
    }
    
    /**
     * Get a Binary value from a row of data.  Convert data if this needed. 
     * 
     * @param rowRow the row of data
     * @param index the index
     * @return The binary found on that position in the row
     * @throws KettleValueException in case there was a problem converting the data.
     */
    public byte[] getBinary(Object[] dataRow, int index) throws KettleValueException
    {
        ValueMetaInterface meta = (ValueMetaInterface) valueMetaList.get(index);
        return meta.getBinary(dataRow[index]);
    }

    /**
     * Determines whether a value in a row is null.  
     * A value is null when the object is null or when it's an empty String 
     * 
     * @param dataRow The row of data 
     * @param index the index to reference
     * @return true if the value on the index is null.
     */
    public boolean isNull(Object[] dataRow, int index)
    {
        return getValueMeta(index).isNull(dataRow[index]);
    }
    
    /**
     * @return a cloned Object[] object.
     * @throws KettleValueException in case something is not quite right with the expected data
     */
    public Object[] cloneRow(Object[] objects) throws KettleValueException
    {
        Object[] newObjects = new Object[objects.length];
        for (int i=0;i<objects.length;i++)
        {
            ValueMetaInterface valueMeta = getValueMeta(i);
            newObjects[i] = valueMeta.cloneValueData(objects[i]);
        }
        return newObjects;
    }

    
    public String getString(Object[] dataRow, String valueName, String defaultValue) throws KettleValueException
    {
        int index = indexOfValue(valueName);
        if (index<1) return defaultValue;
        return getString(dataRow, index);
    }

    public Long getInteger(Object[] dataRow, String valueName, Long defaultValue) throws KettleValueException
    {
        int index = indexOfValue(valueName);
        if (index<1) return defaultValue;
        return getInteger(dataRow, index);
    }

    /**
     * Searches the index of a value meta with a given name
     * @param valueName the name of the value metadata to look for
     * @return the index or -1 in case we didn't find the value
     */
    public int indexOfValue(String valueName)
    {
        for (int i=0;i<valueMetaList.size();i++)
        {
            if (getValueMeta(i).getName().equalsIgnoreCase(valueName)) return i;
        }
        return -1;
    }
    
    /**
     * Searches for a value with a certain name in the value meta list 
     * @param valueName The value name to search for
     * @return The value metadata or null if nothing was found
     */
    public ValueMetaInterface searchValueMeta(String valueName)
    {
        for (int i=0;i<valueMetaList.size();i++)
        {
            ValueMetaInterface valueMeta = getValueMeta(i);
            if (valueMeta.getName().equalsIgnoreCase(valueName)) return valueMeta;
        }
        return null;
    }

    public void addRowMeta(RowMetaInterface rowMeta)
    {
        for (int i=0;i<rowMeta.size();i++)
        {
            addValueMeta(rowMeta.getValueMeta(i));
        }
    }
    
    /**
     * Merge the values of row r to this Row.
     * Merge means: only the values that are not yet in the row are added
     * (comparing on the value name).
     *
     * @param r The row to be merged with this row
     */
    public void mergeRowMeta(RowMetaInterface r)
    {
        for (int x=0;x<r.size();x++)
        {
            ValueMetaInterface field = r.getValueMeta(x);
            if (searchValueMeta(field.getName())==null)
            {
                addValueMeta(field); // Not in list yet: add
            }
        }
    }

    /**
     * Get an array of the names of all the Values in the Row.
     * @return an array of Strings: the names of all the Values in the Row.
     */
    public String[] getFieldNames()
    {
        String retval[] = new String[size()];

        for (int i=0;i<size();i++)
        {
            retval[i]=getValueMeta(i).getName();
        }

        return retval;
    }

    /**
     * Write ONLY the specified data to the outputStream
     * @throws KettleFileException  in case things go awry
     */
    public void writeData(DataOutputStream outputStream, Object[] data) throws KettleFileException
    {
        // Write all values in the row
        for (int i=0;i<size();i++) getValueMeta(i).writeData(outputStream, data[i]);
    }

    /**
     * Write ONLY the specified metadata to the outputStream
     * @throws KettleFileException  in case things go awry
     */
    public void writeMeta(DataOutputStream outputStream) throws KettleFileException
    {
        // First handle the number of fields in a row
        try
        {
            outputStream.writeInt(size());
        }
        catch (IOException e)
        {
            throw new KettleFileException("Unable to write nr of metadata values", e);
        }

        // Write all values in the row
        for (int i=0;i<size();i++) getValueMeta(i).writeMeta(outputStream);

    }
    
    public RowMeta(DataInputStream inputStream) throws KettleFileException
    {
        this();
        
        int nr;
        try
        {
            nr = inputStream.readInt();
        }
        catch (IOException e)
        {
            throw new KettleFileException("Unable to read nr of metadata values", e);
        }
        for (int i=0;i<nr;i++)
        {
            addValueMeta( new ValueMeta(inputStream) );
        }
    }

    public Object[] readData(DataInputStream inputStream) throws KettleFileException
    {
        Object[] data = new Object[size()];
        for (int i=0;i<size();i++)
        {
            data[i] = getValueMeta(i).readData(inputStream);
        }
        return data;
    }

    public void clear()
    {
        valueMetaList.clear();
    }

    public void removeValueMeta(String valueName) throws KettleValueException
    {
        int index = indexOfValue(valueName);
        if (index<0) throw new KettleValueException("Unable to find value metadata with name '"+valueName+"', so I can't delete it.");
        valueMetaList.remove(index);
    }

    public void removeValueMeta(int index)
    {
        valueMetaList.remove(index);
    }
    
    /**
     * Get the string representation of the data in a row of data
     * @param row the row of data to convert to string
     * @return the row of data in string form
     * @throws KettleValueException in case of a conversion error
     */
    public String getString(Object[] row) throws KettleValueException
    {
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<size();i++)
        {
            if (i>0) buffer.append(", ");
            buffer.append( "[" );
            buffer.append( getString(row, i) );
            buffer.append( "]" );
        }
        return buffer.toString();
    }

    /**
     * Get an array of strings showing the name of the values in the row
     * padded to a maximum length, followed by the types of the values.
     *
     * @param maxlen The length to which the name will be padded.
     * @return an array of strings: the names and the types of the fieldnames in the row.
     */
    public String[] getFieldNamesAndTypes(int maxlen)
    {
        String retval[] = new String[size()];

        for (int i=0;i<size();i++)
        {
            ValueMetaInterface v = getValueMeta(i);
            retval[i]= Const.rightPad(v.getName(), maxlen)+"   ("+v.getTypeDesc()+")";
        }

        return retval;
    }
    
    /**
     * Compare 2 rows with each other using certain values in the rows and
     * also considering the specified ascending clauses of the value metadata.

     * @param rowData1 The first row of data
     * @param rowData2 The second row of data
     * @param fieldnrs the fields to compare on (in that order)
     * @return 0 if the rows are considered equal, -1 is data1 is smaller, 1 if data2 is smaller.
     * @throws KettleValueException
     */
    public int compare(Object[] rowData1, Object[] rowData2, int fieldnrs[]) throws KettleValueException
    {
        for (int i=0;i<fieldnrs.length;i++)
        {
            ValueMetaInterface valueMeta = getValueMeta(fieldnrs[i]);
            
            int cmp = valueMeta.compare(rowData1[fieldnrs[i]], rowData2[fieldnrs[i]]);
            if (cmp!=0) return cmp;
        }

        return 0;
    }
    
    
}
