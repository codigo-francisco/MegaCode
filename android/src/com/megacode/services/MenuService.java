package com.megacode.services;

import com.megacode.adapters.model.SkillNode;
import com.megacode.models.TypeLevel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MenuService {

    public static LinkedList<List<SkillNode>> crearRuta()
    {
        LinkedList<List<SkillNode>> nodes = new LinkedList<>();

        //la raiz de ejemplo
        nodes.add(new ArrayList<>(
                SkillNode.buildNodes(
                        new SkillNode("",TypeLevel.COMANDO)
                )
        ));

        nodes.add(new ArrayList<>(
           SkillNode.buildNodes(
                   new SkillNode("",TypeLevel.COMANDO),
                   new SkillNode("",TypeLevel.COMANDO)
           )
        ));

        nodes.add(new ArrayList<>(
                SkillNode.buildNodes(
                        new SkillNode("",TypeLevel.SI)
                )
        ));

        return nodes;
    }

}
