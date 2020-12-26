package com.example.musicstats.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.musicstats.R;

public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(Context context)
    {
        super(context, "reviewDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d("myLogs", "--- onCreate database ---");

        db.execSQL("create table user ("
                + "id integer primary key autoincrement,"
                + "token text" + ");");

        db.execSQL("drop table if exists favourite");
        db.execSQL("create table favourite ("
                + "id integer primary key autoincrement,"
                + "artists text, "
                + "name text,"
                + "uri text unique,"
                + "user_id integer REFERENCES user(id) ON DELETE CASCADE" +");");

        db.execSQL("drop table if exists charts");
        db.execSQL("create table charts ("
                + "id integer primary key autoincrement,"
                + "title text,"
                + "img integer,"
                + "uri text unique,"
                + "latitude real,"
                + "longitude real" + ");");
        fillCharts(db);

        db.execSQL("drop table if exists awards");
        db.execSQL("create table awards ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "img integer,"
                + "amount integer,"
                + "nomination text,"
                + "winner text,"
                + "others text" + ");");
        fillAwards(db);

        db.execSQL("drop table if exists artists");
        db.execSQL("create table artists ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "img integer,"
                + "uri text unique,"
                + "bio text" +");");
        fillArtists(db);

    }

    public void fillCharts(SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        String[] title = {"USA", "Russia", "Australia", "Germany", "Spain", "Japan", "Sweden", "Argentina", "South Africa",
        "Philippines", "Malaysia", "Netherlands", "Poland", "New Zealand", "Italy", "Indonesia", "Egypt"};

        int[] img = {R.drawable.us, R.drawable.ru, R.drawable.au, R.drawable.de, R.drawable.es, R.drawable.jp, R.drawable.se,
        R.drawable.ar, R.drawable.za, R.drawable.ph, R.drawable.ml, R.drawable.nl, R.drawable.pl, R.drawable.nz, R.drawable.it,
        R.drawable.id, R.drawable.eg};

        String[] uri = {"37i9dQZF1DXaqCgtv7ZR3L", "37i9dQZF1DXaC4AfOLHxib", "37i9dQZF1DX1Ah0nVaIfRO", "37i9dQZF1DX4HROODZmf5u",
        "37i9dQZF1DXdo9iIZiH7LB", "37i9dQZF1DWYYQb2mqFd5I", "37i9dQZF1DX91sZGFzTi2i", "37i9dQZF1DWZ557r0pIzNu",
        "37i9dQZF1DXcqO4WihYaac", "37i9dQZF1DX9zthcuQWp21", "37i9dQZF1DWWtI4xJVmfFQ", "37i9dQZF1DWU7JsJYlV18s",
        "37i9dQZF1DX1ShvsspLokt", "37i9dQZF1DXbEq4SYBe9iA", "37i9dQZF1DX7CTLUEV80vD", "37i9dQZF1DX8lb9hHwQhmN", "37i9dQZF1DWUG3EegbQrUI"};

        double[] latitude = {38.327858, 62.333284, -22.528804, 50.379099, 39.747742, 36.371763, 61.572948, -38.383005,
                -30.023440, 12.004942, 3.302864, 52.074105, 52.009234, -43.631304, 41.191883, -8.476714, 26.242192};

        double[] longitude = {-99.985171, 79.149977, 134.733952, 8.835552,  -2.421960,  139.704125, 15.378983,-66.660117,
                24.525754, 123.028992, 103.328346, 5.999365, 20.528597, 172.147971, 13.768849, 114.593940,  30.303320};

        try
        {
            for (int i = 0; i < title.length; i++)
            {
                cv.clear();
                cv.put("title", title[i]);
                cv.put("img", img[i]);
                cv.put("uri", uri[i]);
                cv.put("latitude", latitude[i]);
                cv.put("longitude", longitude[i]);
                db.insert("charts", null, cv);
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    public void fillAwards(SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        String[] name = {"American Music Awards of 2020",
                "63rd Annual Grammy Awards",
                "2020 Billboard Music Awards",
                "2020 iHeartRadio Music Awards",
                "2020 Melon Music Awards",
                "2020 Mnet Asian Music Awards",
                "2020 MTV Europe Music Awards",
                "2020 MTV Video Music Awards",
                "2020 Brit Awards"};

        int[] img = {R.drawable.ama ,R.drawable.grammy, R.drawable.billboard, R.drawable.i_heart, R.drawable.mma,R.drawable.mnet,
                R.drawable.ema,R.drawable.vma,R.drawable.brit};

        int[] amount = {6, 4, 6, 5, 4, 6, 6, 6, 6};

        String[] nomination = {"Artist of the Year",
                "New Artist of the Year",
                "Favorite Music Video",
                "Collaboration of the Year",
                "Favorite Male Artist – Pop/Rock",
                "Favorite Female Artist – Pop/Rock",
                "Record of the Year",
                "Album of the Year",
                "Song of the Year",
                "Best New Artist",
                "Top Artist",
                "Top New Artist",
                "Billboard Chart Achievement (fan-voted)",
                "Top Male Artist",
                "Top Female Artist",
                "Top Duo/Group",
                "Song of the Year",
                "Female Artist of the Year",
                "Male Artist of the Year",
                "Best Duo/Group of the Year",
                "Best Collaboration",
                "Song of the Year (Daesang)",
                "Artist of the Year (Daesang)",
                "Album of the Year (Daesang)",
                "Netizen Popularity Award",
                "Artist of the Year (Daesang)",
                "Song of the Year (Daesang)",
                "Album of the Year (Daesang)",
                "Worldwide Icon of the Year (Daesang)",
                "Best Male Group",
                "Best Female Group",
                "Best Song",
                "Best Video",
                "Best Collaboration",
                "Best Artist",
                "Best Group",
                "Best New",
                "Video of the Year",
                "Song of the Year",
                "Artist of the Year",
                "Best Group",
                "Push Best New Artist",
                "Best Collaboration",
                "British Album of the Year",
                "Song of the Year",
                "British Male Solo Artist",
                "British Female Solo Artist",
                "British Group",
                "Best New Artist"};

        String[] winner = {"Taylor Swift",
                "Doja Cat",
                "Taylor Swift – \"Cardigan\"",
                "Dan + Shay and Justin Bieber – \"10,000 Hours\"",
                "Justin Bieber",
                "Taylor Swift",
                "TBA",
                "TBA",
                "TBA",
                "TBA",
                "Post Malone",
                "Billie Eilish",
                "Harry Styles",
                "Post Malone",
                "Billie Eilish",
                "Jonas Brothers",
                "\"Truth Hurts\" - Lizzo",
                "Billie Eilish",
                "Post Malone",
                "Jonas Brothers",
                "\"Señorita\" – Shawn Mendes and Camila Cabello",
                "BTS – \"Dynamite\"",
                "BTS",
                "BTS – Map of the Soul: 7",
                "BTS",
                "BTS",
                "BTS – \"Dynamite\"",
                "BTS – Map of the Soul: 7",
                "BTS",
                "BTS",
                "Blackpink",
                "BTS — \"Dynamite\"",
                "DJ Khaled (featuring Drake) — \"Popstar\"",
                "Karol G (featuring Nicki Minaj) — \"Tusa\"",
                "Lady Gaga",
                "BTS",
                "Doja Cat",
                "The Weeknd – \"Blinding Lights\"",
                "Lady Gaga with Ariana Grande – \"Rain on Me\"",
                "Lady Gaga",
                "BTS",
                "Doja Cat",
                "Lady Gaga with Ariana Grande – \"Rain on Me\"",
                "Dave – Psychodrama",
                "Lewis Capaldi – \"Someone You Loved\"",
                "Stormzy",
                "Mabel",
                "Foals",
                "Lewis Capaldi"};

        String[] other = {"Justin Bieber\n" +
                "Post Malone\n" +
                "Roddy Ricch\n" +
                "The Weeknd\n",
                "DaBaby\n" +
                        "Lil Baby\n" +
                        "Lewis Capaldi\n" +
                        "Roddy Ricch\n" +
                        "Megan Thee Stallion\n",
                "Doja Cat – \"Say So\"\n" +
                        "Future featuring Drake – \"Life Is Good\"\n" +
                        "Lady Gaga and Ariana Grande – \"Rain on Me\"\n" +
                        "The Weeknd – \"Blinding Lights\"\n",
                "Cardi B featuring Megan Thee Stallion – \"WAP\"\n" +
                        "DaBaby featuring Roddy Ricch – \"Rockstar\"\n" +
                        "Lady Gaga and Ariana Grande – \"Rain On Me\"\n" +
                        "Megan Thee Stallion featuring Beyoncé – \"Savage (Remix)\"\n",
                "Post Malone\n" +
                        "The Weeknd\n",
                "Dua Lipa\n" +
                        "Lady Gaga\n",
                "\"Black Parade\" – Beyoncé\n" +
                        "\"Colors\" – Black Pumas\n" +
                        "\"Rockstar\" – DaBaby featuring Roddy Ricch\n" +
                        "\"Say So\" – Doja Cat\n" +
                        "\"Everything I Wanted\" – Billie Eilish\n" +
                        "\"Don't Start Now\" – Dua Lipa\n" +
                        "\"Circles\" – Post Malone\n" +
                        "\"Savage\" – Megan Thee Stallion featuring Beyoncé\n",
                "Chilombo – Jhené Aiko\n" +
                        "Black Pumas (Deluxe Edition) – Black Pumas\n" +
                        "Everyday Life – Coldplay\n" +
                        "Djesse Vol. 3 – Jacob Collier\n" +
                        "Women in Music Pt. III – Haim\n" +
                        "Future Nostalgia – Dua Lipa\n" +
                        "Hollywood's Bleeding – Post Malone\n" +
                        "Folklore – Taylor Swift\n",
                "\"Black Parade\"\n" +
                        "\"The Box\"\n" +
                        "\"Cardigan\"\n" +
                        "\"Circles\"\n" +
                        "\"Don't Start Now\"\n" +
                        "\"Everything I Wanted\"\n" +
                        "\"I Can't Breathe\"\n" +
                        "\"If the World Was Ending\"\n",
                "Ingrid Andress\n" +
                        "Phoebe Bridgers\n" +
                        "Noah Cyrus\n" +
                        "Chika\n" +
                        "D Smoke\n" +
                        "Doja Cat\n" +
                        "Kaytranada\n" +
                        "Megan Thee Stallion\n",
                "Billie Eilish\n" +
                        "Jonas Brothers\n" +
                        "Khalid\n" +
                        "Taylor Swift\n",
                "DaBaby\n" +
                        "Lil Nas X\n" +
                        "Lizzo\n" +
                        "Roddy Ricch\n",
                "Mariah Carey\n" +
                        "Luke Combs\n" +
                        "Lil Nas X\n" +
                        "Taylor Swift\n",
                "DaBaby\n" +
                        "Khalid\n" +
                        "Lil Nas X\n" +
                        "Ed Sheeran\n",
                "Ariana Grande\n" +
                        "Halsey\n" +
                        "Lizzo\n" +
                        "Taylor Swift\n",
                "BTS\n" +
                        "Dan + Shay\n" +
                        "Maroon 5\n" +
                        "Panic! at the Disco\n",
                "\"Bad Guy\" – Billie Eilish\n" +
                        "\"Old Town Road\" - Lil Nas X\n" +
                        "\"Señorita\" - Shawn Mendes and Camila Cabello\n" +
                        "\"Sucker\" - Jonas Brothers\n",
                "Ariana Grande\n" +
                        "Halsey\n" +
                        "Lizzo\n" +
                        "Taylor Swift\n",
                "Ed Sheeran\n" +
                        "Khalid\n" +
                        "Luke Combs\n" +
                        "Shawn Mendes\n",
                "Dan + Shay\n" +
                        "Imagine Dragons\n" +
                        "Maroon 5\n" +
                        "Panic! at the Disco\n",
                "\"Dancing with a Stranger\" – Sam Smith and Normani\n" +
                        "\"Eastside\" – Benny Blanco, Halsey and Khalid\n" +
                        "\"I Don't Care\" – Ed Sheeran and Justin Bieber\n" +
                        "\"Sunflower\" – Post Malone and Swae Lee\n",
                "Blackpink – \"How You Like That\"\n" +
                        "Yerin Baek – Square (2017)\n" +
                        "IU – \"Eight\" featuring Suga\n" +
                        "Zico – \"Any Song\"\n" +
                        "Red Velvet – \"Psycho\"\n" +
                        "Hwasa – Maria\n" +
                        "MC the Max – Bloom\n" +
                        "Oh My Girl – Nonstop\n" +
                        "SSAK3 – Beach Again\n",
                "Yerin Baek\n" +
                        "IU\n" +
                        "Lim Young-woong\n" +
                        "Zico\n" +
                        "Blackpink\n" +
                        "Baekhyun\n" +
                        "Iz*One\n" +
                        "Kim Ho Joong\n" +
                        "Oh My Girl\n",
                "Iz*One – Bloom*Iz\n" +
                        "Baek Ye-rin – Every Letter I Sent You\n" +
                        "Oh My Girl – Nonstop\n" +
                        "Blackpink – The Album\n" +
                        "Baekhyun – Delight\n" +
                        "Bol4 – Youth Diary II\n" +
                        "Hwasa – María (EP)\n" +
                        "NCT 127 – Neo Zone\n" +
                        "Red Velvet – The ReVe Festival: Finale\n",
                "Iz*One\n" +
                        "Baekhyun\n" +
                        "Blackpink\n" +
                        "Apink\n" +
                        "IU\n" +
                        "Oh My Girl\n" +
                        "Suho\n" +
                        "SSAK3\n" +
                        "Lim Young-woong\n",
                "Blackpink\n" +
                        "IU\n" +
                        "Exo\n" +
                        "Twice\n",
                "Blackpink – \"How You Like That\"\n" +
                        "Red Velvet – \"Psycho\"\n" +
                        "IU – \"Blueming\"\n" +
                        "Zico – \"Any Song\"\n",
                "Blackpink – The Album\n" +
                        "Seventeen – Heng:garæ\n" +
                        "IU – Love Poem\n" +
                        "Baekhyun – Delight\n",
                "NCT\n" +
                        "Treasure\n" +
                        "TXT\n" +
                        "Got7\n" +
                        "Ateez\n" +
                        "Seventeen\n" +
                        "Mamamoo\n" +
                        "Twice\n" +
                        "Blackpink\n",
                "Exo\n" +
                        "Got7\n" +
                        "NCT\n" +
                        "Monsta X\n" +
                        "Seventeen\n",
                "Twice\n" +
                        "Red Velvet\n" +
                        "Mamamoo\n" +
                        "Iz*One\n" +
                        "Oh My Girl\n",
                "DaBaby (featuring Roddy Ricch) — \"Rockstar\"\n" +
                        "Dua Lipa — \"Don't Start Now\"\n" +
                        "Lady Gaga and Ariana Grande – \"Rain on Me\"\n" +
                        "Roddy Ricch — \"The Box\"\n" +
                        "The Weeknd – \"Blinding Lights\"\n",
                "Billie Eilish — \"Everything I Wanted\"\n" +
                        "Cardi B (featuring Megan Thee Stallion) — \"WAP\"\n" +
                        "Karol G (featuring Nicki Minaj) — \"Tusa\"\n" +
                        "Lady Gaga and Ariana Grande – \"Rain on Me\"\n" +
                        "Taylor Swift – \"The Man\"\n" +
                        "The Weeknd – \"Blinding Lights\"\n",
                "Blackpink and Selena Gomez – \"Ice Cream\"\n" +
                        "Cardi B (featuring Megan Thee Stallion) — \"WAP\"\n" +
                        "DaBaby (featuring Roddy Ricch) — \"Rockstar\"\n" +
                        "Justin Bieber (featuring Quavo) — \"Intentions\"\n" +
                        "Lady Gaga and Ariana Grande – \"Rain on Me\"\n" +
                        "Sam Smith and Demi Lovato – \"I'm Ready\"\n",
                "Dua Lipa\n" +
                        "Harry Styles\n" +
                        "Justin Bieber\n" +
                        "Miley Cyrus\n" +
                        "The Weeknd\n",
                "5 Seconds of Summer\n" +
                        "Blackpink\n" +
                        "Chloe x Halle\n" +
                        "CNCO\n" +
                        "Little Mix\n",
                "Benee\n" +
                        "DaBaby\n" +
                        "Jack Harlow\n" +
                        "Roddy Ricch\n" +
                        "Yungblud\n",
                "Billie Eilish – \"Everything I Wanted\"\n" +
                        "Eminem (featuring Juice Wrld) – \"Godzilla\"\n" +
                        "Future (featuring Drake) – \"Life Is Good\"\n" +
                        "Lady Gaga with Ariana Grande – \"Rain on Me\"\n" +
                        "Taylor Swift – \"The Man\"\n",
                "Doja Cat – \"Say So\"\n" +
                        "Billie Eilish – \"Everything I Wanted\"\n" +
                        "Megan Thee Stallion – \"Savage\"\n" +
                        "Post Malone – \"Circles\"\n" +
                        "Roddy Ricch – \"The Box\"\n",
                "Justin Bieber\n" +
                        "DaBaby\n" +
                        "Megan Thee Stallion\n" +
                        "Post Malone\n" +
                        "The Weeknd\n",
                "Now United\n" +
                        "5 Seconds of Summer\n" +
                        "Blackpink\n" +
                        "Chloe x Halle\n" +
                        "CNCO\n" +
                        "Little Mix\n" +
                        "Monsta X\n" +
                        "The 1975\n" +
                        "Twenty One Pilots\n",
                "Lewis Capaldi\n" +
                        "Yungblud\n",
                "Black Eyed Peas (featuring J Balvin) – \"Ritmo (Bad Boys for Life)\"\n" +
                        "Future (featuring Drake) – \"Life Is Good\"\n" +
                        "Ariana Grande and Justin Bieber – \"Stuck with U\"\n" +
                        "Karol G (featuring Nicki Minaj) – \"Tusa\"\n" +
                        "Ed Sheeran (featuring Khalid) – \"Beautiful People\"\n",
                "Harry Styles – Fine Line\n" +
                        "Lewis Capaldi – Divinely Uninspired to a Hellish Extent\n" +
                        "Michael Kiwanuka – Kiwanuka\n" +
                        "Stormzy – Heavy Is the Head\n",
                "AJ Tracey – \"Ladbroke Grove\"\n" +
                        "Calvin Harris and Rag'n'Bone Man – \"Giant\"\n" +
                        "Dave featuring Burna Boy – \"Location\"\n" +
                        "Ed Sheeran and Justin Bieber – \"I Don't Care\" (produced by Fred Again)\n" +
                        "Mabel – \"Don't Call Me Up\"\n" +
                        "Mark Ronson featuring Miley Cyrus – \"Nothing Breaks Like a Heart\"\n" +
                        "Sam Smith and Normani – \"Dancing with a Stranger\"\n" +
                        "Stormzy – \"Vossi Bop\"\n" +
                        "Tom Walker – \"Just You and I\"\n",
                "Dave\n" +
                        "Harry Styles\n" +
                        "Lewis Capaldi\n" +
                        "Michael Kiwanuka\n",
                "Charli XCX\n" +
                        "FKA Twigs\n" +
                        "Freya Ridings\n" +
                        "Mahalia\n",
                "Bastille\n" +
                        "Bring Me the Horizon\n" +
                        "Coldplay\n" +
                        "D-Block Europe\n",
                "Aitch\n" +
                        "Dave\n" +
                        "Mabel\n" +
                        "Sam Fender\n"};

        try
        {
            int k = 0;
            for (int i = 0; i < name.length; i++)
            {
                for(int j = 0; j < amount[i]; j++)
                {
                    cv.clear();
                    cv.put("name", name[i]);
                    cv.put("img", img[i]);
                    cv.put("amount", amount[i]);
                    cv.put("nomination",nomination[k]);
                    cv.put("winner", winner[k]);
                    cv.put("others", other[k]);
                    k++;

                    db.insert("awards", null, cv);
                }
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    public void fillArtists(SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        db.beginTransaction();
        String[] name = {"Bad Bunny", "Jhay Cortez", "Drake", "J Balvin", "Juice WRLD", "Marshmello", "The Weeknd", "BTS",
                "Billie Eilish", "Taylor Swift", "Post Malone", "Travis Scott", "Ariana  Grande",
                "Justin Bieber", "Quavo", "Eminem", "Anuel AA", "Dua Lipa"};

        int[] img = {R.drawable.badbunny, R.drawable.cortez, R.drawable.drake, R.drawable.balvin, R.drawable.juice, R.drawable.marshmello,
        R.drawable.weeknd, R.drawable.bts, R.drawable.eilish, R.drawable.taylor, R.drawable.malone, R.drawable.travis, R.drawable.grande,
        R.drawable.bieber, R.drawable.quavo, R.drawable.eminem, R.drawable.anuel, R.drawable.dua};

        String[] uri = {"37i9dQZF1DX2apWzyECwyZ", "37i9dQZF1DZ06evO0jZKwc", "37i9dQZF1DX7QOv5kjbU68", "37i9dQZF1DX7HGyCQ2dcNx",
        "37i9dQZF1DWYnx77Gg1Rgu", "37i9dQZF1DXbraCN8mShma", "37i9dQZF1DX6bnzK9KPvrz", "37i9dQZF1DX08mhnhv6g9b",
        "37i9dQZF1DX6cg4h2PoN9y", "37i9dQZF1DX5KpP2LN299J", "37i9dQZF1DWSWllk9RmgZM", "37i9dQZF1DWUgX5cUT0GbU",
        "37i9dQZF1DX1PfYnYcpw8w", "37i9dQZF1DXc2aPBXGmXrt", "37i9dQZF1DZ06evO0ukVfW", "37i9dQZF1DX1clOuib1KtQ",
        "37i9dQZF1DX4Xv3b1fFYlX", "37i9dQZF1DX3fRquEp6m8D"};

        String[] bio = {"Bad Bunny is a young urban music singer, rapper and producer originally from Puerto Rico. This talented artist has demonstrated his already overwhelming power, influence and demand with completely sold-out concerts all over Europe, Latin America and the U.S. \n Some of his most recognized singles include “Soy Peor,\" \"Diles,\" \"Tu No Metes Cabra,\" \"Chambea\" and Mia thats out with Drake. Bad Bunny has had a short but stunning career with colossal amounts of success",
                "Jesús Manuel Nieves Cortes nació un 9 de abril y es conocido artísticamente como Jhay Cortez. Joven compositor y artista de origen puertorriqueño, específicamente Rio Piedras, Puerto Rico. A la temprana edad de 11 años, grabó sus primeros tres temas y empezó a escribir música. Entre su repertorio musical se encuentra “Deseos” ft. Bryant Myers, “Se supone” ft. Darell, “Se supone RMX” ft. Darell, Ñengo Flow, Almighty, Miky Woodz, Myke Towers, “All eyes on me” ft. Miky Woodz, y su más reciente lanzamiento “Estan pa mi” ft. JBalvin. Este nuevo artista de la multi-nacional Universal, en conjunto con la marca House of Haze, definitivamente es una promesa y revelación en la industria musical.",
                "Canadian rapper and vocalist Drake sustained a high-level commercial presence shortly after he hit the scene in 2006, whether with his own chart-topping releases or a long string of guest appearances on hits by the likes of Lil Wayne, Rihanna, and A$AP Rocky. Thanks to his introspective rap style, his sensitive R&B crooning, and his gold-touch songwriting, each one of his albums -- from 2011's Take Care to 2018's Scorpion -- topped charts worldwide, and singles like the Grammy-winning \"Hotline Bling\" and many of his mixtapes did too. As his star rose, he helped others along, sponsoring The Weeknd's early work, starting the OVO Sound label, and giving features on his records to up-and-coming acts. By the second decade of his career, Drake's constant chart domination, his Grammy wins and nominations, and his meme-worthy cultural presence made him one of the world's most popular musicians.",
                "José Álvaro Osorio Balvín (J Balvin) (born May 7, 1985) is a Colombian reggaeton singer. He has been referred to as the \"Prince of Reggaeton\" (from Spanish: \"Príncipe del Reggaetón\"), and is one of the best-selling Latin music artists with sales of more than 35 million records (albums and singles) worldwide. Balvin was born in Medellín, Colombia. At age 17, he moved to the United States to learn English, where he lived in both Oklahoma and New York. He then returned to Medellín and gained popularity performing at clubs in the city.",
                "Juice WRLD became a Top Ten hitmaker delivering introspective lyrics atop melodic production, with echoes of Travis Scott and Post Malone. His heartsick 2018 debut Goodbye & Good Riddance (2018), home to his biggest hit \"Lucid Dreams,\" reflected a wide range of stylistic influences: R&B instrumentation, dreamy beats, and even indie rock melodicism. The momentum built by \"Lucid Dreams\" helped land Juice's sophomore effort A Death Race for Love (2019) at the top of the charts. Months later, at the height of his mainstream popularity, he tragically passed away after suffering a medical emergency at Chicago's Midway Airport; he had just turned 21. At the time of his death, Juice had three singles in the Hot 100 and both official LPs in the Top 100.",
                "Enigmatic masked DJ/producer Marshmello makes groove-oriented, synth- and bass-heavy electronic dance music that found mainstream crossover success in the late 2010s. In the spirit of similarly cloaked dance figures like deadmau5 and Daft Punk, Marshmello performs wearing a full-head-covering, in his case a marshmallow mask. Despite his early anonymity, Marshmello's career blew up in 2015 when he began releasing tracks online. From there came shows at New York's Pier 94, Pomona, California's HARD Day of the Dead festival, and Miami Music Week. He also scored a number five Billboard electronic/dance album with his full-length debut, Joytime. Collaborative hits followed, with artists including  Selena Gomez(\"Wolves\"),  Khalid(\"Silence\"), and  Bastille(the platinum hit \"Happier\"). He hit a personal chart peak in 2019 with his revolutionary in-game concert Fortnite Extended Set.",
                "The Weeknd took over pop music & culture on his own terms filtering R&B, Pop,& hip-hop through an ambitious widescreen lens. The multi-platinum 3X GRAMMY Award winner has emerged as one of the most successful & significant artists of the modern era. 2012’s 3X platinum \"Trilogy\" collated 3 breakout mixtapes—House of Balloons, Thursday & Echoes of Silence—into his 1st chart-topping collection followed by his debut LP \"Kiss Land\" in 2013. Two years later, “Earned It (Fifty Shades of Grey)” won “Best R&B Performance” & received an Academy Award nod for “Best Original Song” & 4X Platinum \"Beauty Behind The Madness\" won a GRAMMY for “Best Urban Contemporary Album.” In 2018, \"Starboy\" won the same award, making him the 1st artist ever to win twice. His 6-track project My Dear Melancholy marked his 3rd consecutive #1 bow on the Billboard Top 200, & “Pray For Me” with Kendrick Lamar was featured in the trailer for the Academy Award winning Marvel film Black Panther. In 2020 the 80’s-nostalgic track \"Blinding Lights\" became a worldwide sensation, igniting viral dance challenges across social media, peaking at #1 in 30+ countries & headlining Mercedes Benz EQC campaign.",
                "Record-breaking South Korean boy band BTS (aka Bangtan Boys) deliver an energetic blend of dance-pop and hip-hop with deeply introspective lyrics that helped them build a devoted global following while also making them one of the most successful Korean exports in the world. Debuting in the early 2010s with their Skool trilogy, they steadily expanded their audience until breaking into the mainstream consciousness with the Love Yourself series. After the platinum-certified Love Yourself: Tear became their first number one effort outside of their home country, the compilation Love Yourself: Answer found them topping charts in Canada and Japan. By 2020, international fervor reached a peak with their fourth release Map of the Soul: 7, which topped the charts in over 20 countries. Hitting another milestone that summer, BTS became the first all-South Korean act to top the U.S. singles chart with their retro-disco track \"Dynamite\" from their chart-topping fifth set, Be.",
                "18-year-old global pop-phenom Billie Eilish has fast become one of the biggest stars to emerge since the release of her debut single “ocean eyes,” and continues to shatter the ceiling of music with her genre-defying sound. Fast forward from her humble breakout, Billie’s album WHEN WE ALL FALL ASLEEP, WHERE DO WE GO? debuted at #1 in the Billboard 200 in the U.S, as well as in the UK, Australia, Canada, New Zealand, Netherlands, Belgium, Ireland, Sweden, Norway, Finland and many more. It is now the biggest North American debut of the decade (male, female or group), shifting 313,000 units in the first week and has already hit NUMBER 1 in the Billboard 200 album 3 times since its release in March, earning Billie more than 15 billion combined streams worldwide to date - and has won her 5 GRAMMYs (Best New Artist, Album Of The Year, Record Of The Year, Song Of The Year, Best Pop Vocal Album). Billie is currently on her sold-out WHEN WE ALL FALL ASLEEP, WORLD TOUR and about to embark on her Arena WHERE DO WE GO? WORLD TOUR.",
                "Taylor Swift is that rarest of pop phenomena: a superstar who managed to completely cross over from country to the mainstream. Others have performed similar moves -- notably, Dolly Parton and Willie Nelson both became enduring pop-culture icons based on their '70s work -- but Swift shed her country roots like they were a second skin; it was a necessary molting to reveal she was perhaps the sharpest, savviest populist singer/songwriter of her generation, one who could harness the zeitgeist, make it personal and, just as impressively, perform the reverse. These skills were evident on her earliest hits, especially the neo-tribute \"Tim McGraw,\" but her second album, 2008's Fearless, showcased a songwriter discovering who she was and, in the process, finding a mass audience. Fearless wound up having considerable legs not only in the U.S., where it racked up six platinum singles on the strength of the Top Ten hits \"Love Story\" and \"You Belong with Me,\" but throughout the world, performing particularly well in the U.K., Canada, and Australia.",
                "Within five years of his debut, American rapper and singer/songwriter Post Malone went from genre novelty to certified superstar, amassing a string of Top Ten singles with a hybrid style that joined his unique vocal delivery, pained lyrics, and hip-hop production inspired as much by Tim McGraw as Kanye West. After the release of his 2015 quintuple-platinum single \"White Iverson\" off debut Stoney, he quickly ascended to the top of the pop mainstream in the late 2010s with a succession of multi-platinum hits that included chart-toppers \"Rockstar\" with 21 Savage and \"Psycho\" with Ty Dolla $ign from 2018's number one, Grammy-nominated album Beerbongs & Bentleys and the Grammy-nominated \"Sunflower\" with Swae Lee from 2019's Hollywood's Bleeding.",
                "A Houston-born hip-hop artist and producer affiliated with Kanye West's \"GOOD Music\" and T.I.'s \"Grand Hustle\", Travis Scott became known during the early 2010s for his heavily Auto-Tuned half-sung/half-rapped vocal style. Within seven years of his mainstream arrival via Kanye West's Cruel Summer compilation (2012), on which he served as co-producer and featured artist, Scott attained numerous platinum singles as a lead artist, including a streak of four that began with \"Antidote,\" a track off his number three hit debut album, Rodeo (2015). He followed with a pair of number one full-lengths, Birds in the Trap Sing McKnight (2016) and Astroworld (2018), all the while assisting in platinum singles headlined by the likes of  Rihanna(\"Bitch Better Have My Money\"),  SZA(\"Love Galore\"), and  Drake(\"Portland\"), and working extensively with Quavo as Huncho Jack. Whether leading or supporting, Scott's presence was unmistakable, almost always colored with an array of ad-lib trills including but not limited to \"It's lit,\" \"Yeah, yeah,\" and \"Straight up.\"",
                "Ariana Grande is perhaps the quintessential pop star of the last half of the 2010s, capturing the era's spirit and style. Emerging in 2013 with the hit single \"The Way,\" Grande initially appeared to be the heir to the throne of Mariah Carey, due in part to her powerhouse vocals. With its Babyface production, her debut Yours Truly underscored her debt to '90s R&B, but Grande quickly incorporated hip-hop and EDM into her music. \"Problem,\" a 2014 smash duet with Iggy Azalea, was the first indication of her development, an evolution underscored by the hits \"Bang Bang\" and \"Love Me Harder,\" which featured Jessie J & Nicki Minaj and The Weeknd, respectively. Grande maintained her popularity with 2016's Dangerous Woman, then really hit her stride with 2018's Sweetener and its swift sequel Thank U, Next, whose title track became her first number one pop hit. That achievement was quickly equaled by \"7 Rings,\" a glitzy anthem for the Instagram age that consolidated her stardom and artistry, as well as \"Positions,\" the lead single from 2020's R&B-heavy album of the same name.",
                "After closing out 2020 with three smash singles - “Holy,” with Chance The Rapper, “Lonely” with benny blanco, and “Monster” with Shawn Mendes – Justin Bieber reigns as one of the biggest artists in the word: the #1 artist on YouTube with 60mm subscribers, the #2 artist on Spotify global with over 65mm monthly listeners, over 200mm in combined U.S. radio audience per week, and three Grammy nominations and an American Music Award for his 2020 album Changes.",
                "Primarily known for being one-third of the hip-hop/trap collective Migos, Quavo (real name Quavious Marshall) is a rapper and hip-hop artist from Lawrenceville, Georgia. Alongside  Offset(his cousin) and Takeoff (his nephew), he helped form Migos in 2009 and they released their first mixtapes, Juug Season in 2011 and No Label in 2012. (No Label went on to receive a sequel in 2014.) However, it wasn't until the release of the track \"Versace\" from their 2013 mixtape, Young Rich N*ggas, that they came to full prominence. The song was a breakout hit, receiving further exposure when Drake produced a remix. In 2014, Quavo was named \"most influential rapper\" by the online hip-hop community Complex Music. In subsequent years, he appeared on further releases with Migos, most notably their full-length albums Yung Rich Nation (2015) and C U L T U R E (2017). ",
                "Eminem is one of the best-selling artists in music history, easily the biggest crossover success ever seen in rap. To call him hip-hop's Elvis Presley is correct to a degree, but it's largely inaccurate. Certainly, he was the first white rapper since Beastie Boys to garner both sales and critical respect, but his impact has exceeded this confining distinction. On sheer verbal skills, Eminem is one of the greatest MCs of his generation: rapid, fluid, dexterous, and unpredictable, capable of pulling off long-form narratives or withering asides. And thanks to his mentor Dr. Dre, he's had music to match: thick, muscular loops evoking the terror and paranoia conjured by Em's lyrics. To be certain, a great deal of the controversy Eminem courted -- during the turn of the millennium, there was no greater pop cultural bogeyman than Marshall Mathers -- came through in how his violent fantasias, often directed at his mother or his wife, intertwined with flights of absurdity that appealed to listeners too young to absorb the psychodramas Eminem explored on his hit albums, The Slim Shady LP and The Marshall Mathers LP.",
                "Emmanuel Gazmey Santiago, known professionally as Anuel AA, is a retired Puerto Rican rapper and singer. He is considered a pioneer of the Latin trap movement and his lyrics often discuss crime and urban life in Puerto Rico. His music often contains samples and interpolations of songs that were popular during his youth. He is seen as a controversial figure in the Latin music scene for his legal troubles and feuds with fellow Puerto Rican rappers Cosculluela and Ivy Queen and with the American rapper 6ix9ine.",
                "Global pop superstar Dua Lipa released Future Nostalgia, her #1 UK sophomore album, this year to worldwide acclaim. It is one of the best reviewed albums of 2020 and debuted in the top 5 of the Billboard 200 Album Chart. Upon release, Future Nostalgia was the most streamed album in a day by a British female artist globally in Spotify history and has over 4.5 billion streams to date. Dua is the biggest female artist in the world on Spotify and is currently the third biggest artist overall with nearly 60 million monthly listeners. The album’s certified platinum lead single “Don’t Start Now” is a worldwide hit with one billion streams on Spotify alone, and a #2 spot on the Billboard Hot 100, a career high for the pop star. The track also broke her personal best record of weeks at #1 at US Top 40 radio. Dua followed the success of “Don’t Start Now” by releasing smash UK single “Physical,” and her US Top 40 #1 “Break My Heart.” Most recently, Future Nostalgia was shortlisted for UK’s prestigious Mercury Prize. Future Nostalgia is the follow up to Dua’s eponymous 2017 debut, which is certified platinum and spawned 6 platinum tracks."};

        try
        {
            for (int i = 0; i < name.length; i++)
            {
                cv.clear();
                cv.put("name", name[i]);
                cv.put("img", img[i]);
                cv.put("uri", uri[i]);
                cv.put("bio", bio[i]);

                db.insert("artists", null, cv);
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
