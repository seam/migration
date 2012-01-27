package org.open18.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("golferList")
public class GolferList extends EntityQuery
{
    @Override
    public String getEjbql() 
    { 
        return "select golfer from Golfer golfer";
    }
}
