package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.StrictMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Weka
{
    Context context;

    Instance userInstance;

    String beautyAttribute, booksAttribute, businessAttribute, cookingAttribute, communicationAttribute, datingAttribute, dietingAttribute, educationAttribute, fashionAttribute, fitnessAttribute, financeAttribute, foodAttribute, gamesAttribute, healthAttribute, musicAttribute, shoppingAttribute, socialAttribute, travelAttribute;

    MyAttributes.Attributes myAttributes = new MyAttributes.Attributes();

    List<String> types = new ArrayList<>();
    List<String> ages = new ArrayList<>();

    public Weka(MainActivity mainActivity)
    {
        context = mainActivity;
    }

    public String PredictGender()
    {
        try
        {
            //Open the dataset that we would like to evaluate from res/raw
            InputStream inputStream = context.getResources().openRawResource(R.raw.data_gender);
            ConverterUtils.DataSource dataSourceGender = new ConverterUtils.DataSource(inputStream);

            Instances dataGender = dataSourceGender.getDataSet();

            if (dataGender.classIndex() == -1)
            {
                dataGender.setClassIndex(dataGender.numAttributes() - 1);
            }

            //Select an algorithm  to use, J48  classifier which learns decision trees
            J48 treeGender = new J48();
            treeGender.buildClassifier(dataGender);

            //Evaluate predictive accurancy
            Evaluation evalGender = new Evaluation(dataGender);
            evalGender.crossValidateModel(treeGender, dataGender, 10, new java.util.Random(1));

            //Predict
            userInstance = new Instance(dataGender.numAttributes());

            SetAttributes(dataGender);

            userInstance.setDataset(dataGender);

            //compare the predicted class with the actual class
            double predictionGender = evalGender.evaluateModelOnce(treeGender, userInstance);
            return CalculateGender(predictionGender);
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    public String PredictAge()
    {
        try
        {
            //Open the dataset that we would like to evaluate from res/raw
            InputStream inputStream = context.getResources().openRawResource(R.raw.data_age);
            ConverterUtils.DataSource dataSourceAge = new ConverterUtils.DataSource(inputStream);

            Instances dataAge = dataSourceAge.getDataSet();

            if (dataAge.classIndex() == -1)
            {
                dataAge.setClassIndex(dataAge.numAttributes() - 1);
            }

            //Select an algorithm  to use, J48  classifier which learns decision trees
            J48 treeAge = new J48();
            treeAge.buildClassifier(dataAge);

            //Evaluate predictive accurancy
            Evaluation evalAge = new Evaluation(dataAge);
            evalAge.crossValidateModel(treeAge, dataAge, 10, new java.util.Random(1));

            //Predict
            userInstance = new Instance(dataAge.numAttributes());

            SetAttributes(dataAge);

            dataAge.add(userInstance);
            userInstance.setDataset(dataAge);

            double predictionAge = evalAge.evaluateModelOnce(treeAge, userInstance);//compare the predicted class with the actual class
            return CalculateAge(predictionAge);
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    private void SetAttributes(Instances data)
    {
        beautyAttribute = "false";
        if (myAttributes.Beauty)
            beautyAttribute = "true";

        booksAttribute = "false";
        if (myAttributes.Books)
            booksAttribute = "true";

        businessAttribute = "false";
        if (myAttributes.Business)
            businessAttribute = "true";

        cookingAttribute = "false";
        if (myAttributes.Cooking)
            cookingAttribute = "true";

        communicationAttribute = "false";
        if (myAttributes.Communication)
            communicationAttribute = "true";

        educationAttribute = "false";
        if (myAttributes.Education)
            educationAttribute = "true";

        dietingAttribute = "false";
        if (myAttributes.Dieting)
            dietingAttribute = "true";

        fashionAttribute = "false";
        if (myAttributes.Fashion)
            fashionAttribute = "true";

        financeAttribute = "false";
        if (myAttributes.Finance)
            financeAttribute = "true";

        fitnessAttribute = "false";
        if (myAttributes.Fitness)
            fitnessAttribute = "true";

        foodAttribute = "false";
        if (myAttributes.Food)
            foodAttribute = "true";

        gamesAttribute = "false";
        if (myAttributes.Games)
            gamesAttribute = "true";

        datingAttribute = "false";
        if (myAttributes.Dating)
            datingAttribute = "true";

        healthAttribute = "false";
        if (myAttributes.Health)
            healthAttribute = "true";

        musicAttribute = "false";
        if (myAttributes.Music)
            musicAttribute = "true";

        shoppingAttribute = "false";
        if (myAttributes.Shopping)
            shoppingAttribute = "true";

        socialAttribute = "false";
        if (myAttributes.Social)
            socialAttribute = "true";

        travelAttribute = "false";
        if (myAttributes.Travel)
            travelAttribute = "true";


        userInstance.setValue(data.attribute(0), beautyAttribute);
        userInstance.setValue(data.attribute(1), booksAttribute);
        userInstance.setValue(data.attribute(2), cookingAttribute);
        userInstance.setValue(data.attribute(3), dietingAttribute);
        userInstance.setValue(data.attribute(4), educationAttribute);
        userInstance.setValue(data.attribute(5), fashionAttribute);
        userInstance.setValue(data.attribute(6), financeAttribute);
        userInstance.setValue(data.attribute(7), fitnessAttribute);
        userInstance.setValue(data.attribute(8), gamesAttribute);
        userInstance.setValue(data.attribute(9), datingAttribute);
        userInstance.setValue(data.attribute(10), healthAttribute);
        userInstance.setValue(data.attribute(11), businessAttribute);
        userInstance.setValue(data.attribute(12), communicationAttribute);
        userInstance.setValue(data.attribute(13), foodAttribute);
        userInstance.setValue(data.attribute(14), musicAttribute);
        userInstance.setValue(data.attribute(15), shoppingAttribute);
        userInstance.setValue(data.attribute(16), socialAttribute);
        userInstance.setValue(data.attribute(17), travelAttribute);
    }

    private String CalculateGender(double predictionSex)
    {
        String gender = "";

        if (predictionSex == 0)
            gender = "woman";
        else
            gender = "man";

        return gender;
    }

    private String CalculateAge(double predictionAge)
    {
        String age = "";

        if (predictionAge == 0)
        {
            age = "18-";

            //if there is an app with label "PEGI 18" then the user's age must be over 18
            for(String pegi: ages)
            {
                if(pegi.contains("18"))
                {
                    age =  "19-29";
                    break;
                }
            }
        }
        else if (predictionAge == 1)
            age = "19-29";
        else if (predictionAge == 2)
            age = "30-45";
        else
            age = "46+";

        return age;
    }

    public void StartCrawler()
    {
        try
        {
            List<String> packages = GetInstalledApps();

            for (int i = 0; i < packages.size(); i++)
            {
                try
                {
                    //StrictMode is used to catch accidental network access on the application's main thread
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    //Creates a new connection to a URL, use to fetch and parse HTML
                    Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packages.get(i)).get();

                    //TYPE
                    //find elements that matches the cssQuery
                    Elements spanElements = doc.select("span");

                    int count = 0;
                    for (Element element : spanElements)
                    {
                        if (element.attr("class").contains("T32cc UAO9ie"))
                        {
                            if (count == 0)
                            {
                                count++;
                            }
                            else
                            {
                                Elements classElements = element.getElementsByAttribute("class");
                                Node node = classElements.get(1).childNodes().get(0);
                                types.add(node.toString());
                                break;
                            }
                        }
                    }

                    //age
                    spanElements = doc.select("div");

                    for (Element element : spanElements)
                    {
                        if (element.attr("class").contains("KmO8jd"))
                        {
                            Elements classElements = element.getElementsByAttribute("class");
                            Node node = classElements.get(0).childNodes().get(1);
                            ages.add(node.toString());
                            break;
                        }
                    }
                }
                catch (Exception ex)
                {
                    continue;
                }
            }

            for (String type : types)
            {
                if (type.contains("Music"))
                    myAttributes.Music = true;
                else if (type.contains("Communication"))
                    myAttributes.Communication = true;
                else if (type.contains("Shopping"))
                    myAttributes.Shopping = true;
                else if (type.contains("Education"))
                    myAttributes.Education = true;
                else if (type.contains("Travel"))
                    myAttributes.Travel = true;
                else if (type.contains("Food"))
                    myAttributes.Food = true;
                else if (type.contains("Social"))
                    myAttributes.Social = true;
                else if (type.contains("Finance"))
                    myAttributes.Finance = true;
                else if (type.contains("Business"))
                    myAttributes.Business = true;
                else if (type.contains("Books"))
                    myAttributes.Books = true;
                else if (type.contains("Beauty"))
                    myAttributes.Beauty = true;
                else if (type.contains("Dating"))
                    myAttributes.Dating = true;
                else if (type.contains("Health"))
                    myAttributes.Health = true;
                else if (type.contains("Cooking"))
                    myAttributes.Cooking = true;
                else if (type.contains("Fashion"))
                    myAttributes.Fashion = true;
                else if (type.contains("Fitness"))
                    myAttributes.Fitness = true;
                else if (type.contains("Games"))
                    myAttributes.Games = true;
            }
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    private List<String> GetInstalledApps()
    {
        try
        {
            //get installed apps using Pakcage Manager
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);

            //a list that contains only the packages
            List<String> packages = new ArrayList<>();

            for (ResolveInfo app : pkgAppsList)
            {
                packages.add(app.activityInfo.packageName);
            }

            return packages;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}