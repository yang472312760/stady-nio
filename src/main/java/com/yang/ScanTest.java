package com.yang;

import java.util.Scanner;

/**
 * <p>@ProjectName:stady-nio</p>
 * <p>@Package:com.yang</p>
 * <p>@ClassName:ScanTest</p>
 * <p>@Description:${description}</p>
 * <p>@Author:yang</p>
 * <p>@Date:2021/2/5 14:31</p>
 * <p>@Version:1.0</p>
 */
public class ScanTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String next = scanner.next();
            System.out.println(next);
        }


    }

}
