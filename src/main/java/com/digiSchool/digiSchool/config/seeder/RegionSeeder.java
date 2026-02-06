package com.digiSchool.digiSchool.config.seeder;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.Exceptionconfig.model.Arrondissement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Departement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.Region;
import com.digiSchool.digiSchool.Exceptionconfig.model.Ville;
import com.digiSchool.digiSchool.Exceptionconfig.repository.ArrondissementRepository;
import com.digiSchool.digiSchool.Exceptionconfig.repository.DepartementRepository;
import com.digiSchool.digiSchool.Exceptionconfig.repository.QuartierRepository;
import com.digiSchool.digiSchool.Exceptionconfig.repository.VilleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.RegionRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Seeder pour la geographie du Cameroun.
 * Cree les 10 regions avec leurs departements, arrondissements, villes et quartiers.
 */
@Component
public class RegionSeeder {

    private final RegionRepository regionRepository;
    private final DepartementRepository departementRepository;
    private final ArrondissementRepository arrondissementRepository;
    private final VilleRepository villeRepository;
    private final QuartierRepository quartierRepository;

    // Stockage des quartiers pour les ecoles
    private Map<String, Quartier> quartiersMap = new HashMap<>();

    public RegionSeeder(RegionRepository regionRepository,
                        DepartementRepository departementRepository,
                        ArrondissementRepository arrondissementRepository,
                        VilleRepository villeRepository,
                        QuartierRepository quartierRepository) {
        this.regionRepository = regionRepository;
        this.departementRepository = departementRepository;
        this.arrondissementRepository = arrondissementRepository;
        this.villeRepository = villeRepository;
        this.quartierRepository = quartierRepository;
    }

    /**
     * Cree toutes les donnees geographiques du Cameroun
     */
    public void seed() {
        if (regionRepository.count() > 0) {
            System.out.println("  -> Geographie : deja presente, skip");
            loadExistingQuartiers();
            return;
        }

        seedAdamaoua();
        seedCentre();
        seedEst();
        seedExtremeNord();
        seedLittoral();
        seedNord();
        seedNordOuest();
        seedOuest();
        seedSud();
        seedSudOuest();

        System.out.println("  -> Geographie : 10 regions du Cameroun creees");
    }

    /**
     * Recupere un quartier par son code
     */
    public Quartier getQuartier(String code) {
        return quartiersMap.get(code);
    }

    // ========================= ADAMAOUA =========================
    private void seedAdamaoua() {
        Region adamaoua = createRegion("AD", "Adamaoua");

        Departement djere = createDepartement("DJR", "Djerem", adamaoua);
        Departement faro_et_deo = createDepartement("FRD", "Faro-et-Deo", adamaoua);
        Departement mayo_banyo = createDepartement("MBY", "Mayo-Banyo", adamaoua);
        Departement mbere = createDepartement("MBR", "Mbere", adamaoua);
        Departement vina = createDepartement("VNA", "Vina", adamaoua);

        Arrondissement ngaoundere1 = createArrondissement("NDE1", "Ngaoundere 1er", vina);
        createArrondissement("NDE2", "Ngaoundere 2eme", vina);
        createArrondissement("NDE3", "Ngaoundere 3eme", vina);
        Arrondissement tibati = createArrondissement("TBT", "Tibati", djere);
        Arrondissement tignere = createArrondissement("TGR", "Tignere", faro_et_deo);
        Arrondissement banyo = createArrondissement("BNY", "Banyo", mayo_banyo);
        Arrondissement meiganga = createArrondissement("MGG", "Meiganga", mbere);

        Ville ngaoundereVille = createVille("VNDE", "Ngaoundere", ngaoundere1);
        createVille("VTBT", "Tibati", tibati);
        Ville banyoVille = createVille("VBNY", "Banyo", banyo);
        createVille("VMGG", "Meiganga", meiganga);
        createVille("VTGR", "Tignere", tignere);

        createQuartier("QJS", "Joli-Soir", ngaoundereVille);
        createQuartier("QBL", "Baladji", ngaoundereVille);
        createQuartier("QMB", "Mbideng", ngaoundereVille);
        createQuartier("QON", "Onaref", ngaoundereVille);
        createQuartier("QBNC", "Centre Banyo", banyoVille);
    }

    // ========================= CENTRE =========================
    private void seedCentre() {
        Region centre = createRegion("CE", "Centre");

        Departement haute_sanaga = createDepartement("HSG", "Haute-Sanaga", centre);
        Departement lekie = createDepartement("LKE", "Lekie", centre);
        Departement mbam_et_inoubou = createDepartement("MBI", "Mbam-et-Inoubou", centre);
        Departement mbam_et_kim = createDepartement("MBK", "Mbam-et-Kim", centre);
        Departement mefou_et_afamba = createDepartement("MFA", "Mefou-et-Afamba", centre);
        Departement mefou_et_akono = createDepartement("MAK", "Mefou-et-Akono", centre);
        Departement mfoundi = createDepartement("MFD", "Mfoundi", centre);
        Departement nyong_et_kelle = createDepartement("NYK", "Nyong-et-Kelle", centre);
        Departement nyong_et_mfoumou = createDepartement("NYM", "Nyong-et-Mfoumou", centre);
        Departement nyong_et_so = createDepartement("NYS", "Nyong-et-So'o", centre);

        Arrondissement yaounde1 = createArrondissement("YDE1", "Yaounde 1er", mfoundi);
        createArrondissement("YDE2", "Yaounde 2eme", mfoundi);
        createArrondissement("YDE3", "Yaounde 3eme", mfoundi);
        createArrondissement("YDE4", "Yaounde 4eme", mfoundi);
        createArrondissement("YDE5", "Yaounde 5eme", mfoundi);
        createArrondissement("YDE6", "Yaounde 6eme", mfoundi);
        createArrondissement("YDE7", "Yaounde 7eme", mfoundi);
        Arrondissement monatele = createArrondissement("MNT", "Monatele", lekie);
        Arrondissement obala = createArrondissement("OBL", "Obala", lekie);
        createArrondissement("OKL", "Okola", lekie);
        createArrondissement("SAA", "Saa", lekie);
        Arrondissement nanga_eboko = createArrondissement("NEB", "Nanga-Eboko", haute_sanaga);
        Arrondissement bafia = createArrondissement("BFA", "Bafia", mbam_et_inoubou);
        createArrondissement("NTU", "Ntui", mbam_et_kim);
        Arrondissement mfou = createArrondissement("MFU", "Mfou", mefou_et_afamba);
        createArrondissement("NGM", "Ngoumou", mefou_et_akono);
        Arrondissement eseka = createArrondissement("ESK", "Eseka", nyong_et_kelle);
        Arrondissement akonolinga = createArrondissement("AKN", "Akonolinga", nyong_et_mfoumou);
        Arrondissement mbalmayo = createArrondissement("MBM", "Mbalmayo", nyong_et_so);

        Ville yaoundeVille = createVille("VYDE", "Yaounde", yaounde1);
        Ville obalaVille = createVille("VOBL", "Obala", obala);
        createVille("VMNT", "Monatele", monatele);
        Ville bafiaVille = createVille("VBFA", "Bafia", bafia);
        createVille("VMFU", "Mfou", mfou);
        createVille("VMBM", "Mbalmayo", mbalmayo);
        createVille("VESK", "Eseka", eseka);
        createVille("VNEB", "Nanga-Eboko", nanga_eboko);
        createVille("VAKN", "Akonolinga", akonolinga);

        // Quartiers de Yaounde (utilises pour les ecoles)
        createQuartier("QBS", "Bastos", yaoundeVille);
        createQuartier("QNL", "Nlongkak", yaoundeVille);
        createQuartier("QMM", "Mvog-Mbi", yaoundeVille);
        createQuartier("QMV", "Mvan", yaoundeVille);
        createQuartier("QBA", "Biyem-Assi", yaoundeVille);
        createQuartier("QES", "Essos", yaoundeVille);
        createQuartier("QEM", "Etoa-Meki", yaoundeVille);
        createQuartier("QNK", "Nkolbisson", yaoundeVille);
        createQuartier("QMS", "Messa", yaoundeVille);
        createQuartier("QOM", "Omnisport", yaoundeVille);
        createQuartier("QMD", "Mendong", yaoundeVille);
        createQuartier("QNS", "Nsimeyong", yaoundeVille);
        createQuartier("QMK", "Mokolo", yaoundeVille);
        createQuartier("QTS", "Tsinga", yaoundeVille);
        createQuartier("QNG", "Ngousso", yaoundeVille);
        createQuartier("QEA", "Emana", yaoundeVille);
        createQuartier("QOBC", "Centre Obala", obalaVille);
        createQuartier("QBFC", "Centre Bafia", bafiaVille);
    }

    // ========================= EST =========================
    private void seedEst() {
        Region est = createRegion("ES", "Est");

        Departement boumba_et_ngoko = createDepartement("BNG", "Boumba-et-Ngoko", est);
        Departement haut_nyong = createDepartement("HNY", "Haut-Nyong", est);
        Departement kadey = createDepartement("KDY", "Kadey", est);
        Departement lom_et_djerem = createDepartement("LDJ", "Lom-et-Djerem", est);

        Arrondissement bertoua1 = createArrondissement("BTA1", "Bertoua 1er", lom_et_djerem);
        createArrondissement("BTA2", "Bertoua 2eme", lom_et_djerem);
        Arrondissement abong_mbang = createArrondissement("ABM", "Abong-Mbang", haut_nyong);
        Arrondissement batouri = createArrondissement("BTR", "Batouri", kadey);
        Arrondissement yokadouma = createArrondissement("YKD", "Yokadouma", boumba_et_ngoko);
        createArrondissement("BLB", "Belabo", lom_et_djerem);

        Ville bertouaVille = createVille("VBTA", "Bertoua", bertoua1);
        createVille("VABM", "Abong-Mbang", abong_mbang);
        Ville batouriVille = createVille("VBTR", "Batouri", batouri);
        createVille("VYKD", "Yokadouma", yokadouma);

        createQuartier("QBTC", "Centre Bertoua", bertouaVille);
        createQuartier("QHB", "Haoussa", bertouaVille);
        createQuartier("QBTRC", "Centre Batouri", batouriVille);
    }

    // ========================= EXTREME-NORD =========================
    private void seedExtremeNord() {
        Region extreme_nord = createRegion("EN", "Extreme-Nord");

        Departement diamare = createDepartement("DMR", "Diamare", extreme_nord);
        Departement logone_et_chari = createDepartement("LGC", "Logone-et-Chari", extreme_nord);
        Departement mayo_danay = createDepartement("MYD", "Mayo-Danay", extreme_nord);
        Departement mayo_kani = createDepartement("MYK", "Mayo-Kani", extreme_nord);
        Departement mayo_sava = createDepartement("MYS", "Mayo-Sava", extreme_nord);
        Departement mayo_tsanaga = createDepartement("MYT", "Mayo-Tsanaga", extreme_nord);

        Arrondissement maroua1 = createArrondissement("MRA1", "Maroua 1er", diamare);
        createArrondissement("MRA2", "Maroua 2eme", diamare);
        createArrondissement("MRA3", "Maroua 3eme", diamare);
        Arrondissement kousseri = createArrondissement("KSR", "Kousseri", logone_et_chari);
        Arrondissement yagoua = createArrondissement("YGA", "Yagoua", mayo_danay);
        Arrondissement kaele = createArrondissement("KLE", "Kaele", mayo_kani);
        Arrondissement mora = createArrondissement("MOR", "Mora", mayo_sava);
        Arrondissement mokolo_arr = createArrondissement("MKL", "Mokolo", mayo_tsanaga);

        Ville marouaVille = createVille("VMRA", "Maroua", maroua1);
        Ville kousseriVille = createVille("VKSR", "Kousseri", kousseri);
        createVille("VYGA", "Yagoua", yagoua);
        createVille("VKLE", "Kaele", kaele);
        createVille("VMOR", "Mora", mora);
        createVille("VMKL", "Mokolo", mokolo_arr);

        createQuartier("QDM", "Domayo", marouaVille);
        createQuartier("QPT", "Pitoare", marouaVille);
        createQuartier("QKK", "Kakatare", marouaVille);
        createQuartier("QPV", "Pont-Vert", marouaVille);
        createQuartier("QKSC", "Centre Kousseri", kousseriVille);
    }

    // ========================= LITTORAL =========================
    private void seedLittoral() {
        Region littoral = createRegion("LT", "Littoral");

        Departement moungo = createDepartement("MNG", "Moungo", littoral);
        Departement nkam = createDepartement("NKM", "Nkam", littoral);
        Departement sanaga_maritime = createDepartement("SGM", "Sanaga-Maritime", littoral);
        Departement wouri = createDepartement("WRI", "Wouri", littoral);

        Arrondissement douala1 = createArrondissement("DLA1", "Douala 1er", wouri);
        createArrondissement("DLA2", "Douala 2eme", wouri);
        createArrondissement("DLA3", "Douala 3eme", wouri);
        createArrondissement("DLA4", "Douala 4eme", wouri);
        createArrondissement("DLA5", "Douala 5eme", wouri);
        Arrondissement nkongsamba1 = createArrondissement("NKS1", "Nkongsamba 1er", moungo);
        createArrondissement("NKS2", "Nkongsamba 2eme", moungo);
        Arrondissement loum = createArrondissement("LOM", "Loum", moungo);
        createArrondissement("MNJ", "Manjo", moungo);
        Arrondissement edea1 = createArrondissement("EDA1", "Edea 1er", sanaga_maritime);
        createArrondissement("EDA2", "Edea 2eme", sanaga_maritime);
        Arrondissement yabassi = createArrondissement("YBS", "Yabassi", nkam);

        Ville doualaVille = createVille("VDLA", "Douala", douala1);
        Ville nkongsamba = createVille("VNKS", "Nkongsamba", nkongsamba1);
        createVille("VLOM", "Loum", loum);
        createVille("VEDA", "Edea", edea1);
        createVille("VYBS", "Yabassi", yabassi);

        // Quartiers de Douala (utilises pour les ecoles)
        createQuartier("QBN", "Bonamoussadi", doualaVille);
        createQuartier("QAK", "Akwa", doualaVille);
        createQuartier("QDD", "Deido", doualaVille);
        createQuartier("QBL2", "Bali", doualaVille);
        createQuartier("QBB", "Bonaberi", doualaVille);
        createQuartier("QNB", "New-Bell", doualaVille);
        createQuartier("QBP", "Bonapriso", doualaVille);
        createQuartier("QMK2", "Makepe", doualaVille);
        createQuartier("QLG", "Logpom", doualaVille);
        createQuartier("QKT", "Kotto", doualaVille);
        createQuartier("QND", "Ndogbong", doualaVille);
        createQuartier("QCS", "Cite SIC", doualaVille);
        createQuartier("QNKC", "Centre Nkongsamba", nkongsamba);
    }

    // ========================= NORD =========================
    private void seedNord() {
        Region nord = createRegion("NO", "Nord");

        Departement benoue = createDepartement("BNE", "Benoue", nord);
        Departement faro = createDepartement("FAR", "Faro", nord);
        Departement mayo_louti = createDepartement("MYL", "Mayo-Louti", nord);
        Departement mayo_rey = createDepartement("MYR", "Mayo-Rey", nord);

        Arrondissement garoua1 = createArrondissement("GRA1", "Garoua 1er", benoue);
        createArrondissement("GRA2", "Garoua 2eme", benoue);
        createArrondissement("GRA3", "Garoua 3eme", benoue);
        createArrondissement("POL", "Poli", faro);
        Arrondissement guider = createArrondissement("GDR", "Guider", mayo_louti);
        Arrondissement tcholliré = createArrondissement("TCL", "Tchollire", mayo_rey);
        createArrondissement("RYB", "Rey-Bouba", mayo_rey);

        Ville garouaVille = createVille("VGRA", "Garoua", garoua1);
        createVille("VGDR", "Guider", guider);
        createVille("VTCL", "Tchollire", tcholliré);

        createQuartier("QYL", "Yelwa", garouaVille);
        createQuartier("QRM", "Roumde-Adjia", garouaVille);
        createQuartier("QLP", "Lopere", garouaVille);
        createQuartier("QPP", "Poumpoumre", garouaVille);
    }

    // ========================= NORD-OUEST =========================
    private void seedNordOuest() {
        Region nord_ouest = createRegion("NW", "Nord-Ouest");

        Departement boyo = createDepartement("BYO", "Boyo", nord_ouest);
        Departement bui = createDepartement("BUI", "Bui", nord_ouest);
        Departement donga_mantung = createDepartement("DGM", "Donga-Mantung", nord_ouest);
        Departement menchum = createDepartement("MCH", "Menchum", nord_ouest);
        Departement mezam = createDepartement("MZM", "Mezam", nord_ouest);
        Departement momo = createDepartement("MMO", "Momo", nord_ouest);
        Departement ngo_ketunjia = createDepartement("NGK", "Ngo-Ketunjia", nord_ouest);

        Arrondissement bamenda1 = createArrondissement("BDA1", "Bamenda 1er", mezam);
        createArrondissement("BDA2", "Bamenda 2eme", mezam);
        createArrondissement("BDA3", "Bamenda 3eme", mezam);
        Arrondissement kumbo = createArrondissement("KMB", "Kumbo", bui);
        Arrondissement nkambe = createArrondissement("NKB", "Nkambe", donga_mantung);
        Arrondissement wum = createArrondissement("WUM", "Wum", menchum);
        createArrondissement("FDG", "Fundong", boyo);
        createArrondissement("MBW", "Mbengwi", momo);
        Arrondissement ndop = createArrondissement("NDP", "Ndop", ngo_ketunjia);

        Ville bamendaVille = createVille("VBDA", "Bamenda", bamenda1);
        Ville kumboVille = createVille("VKMB", "Kumbo", kumbo);
        createVille("VNKB", "Nkambe", nkambe);
        createVille("VWUM", "Wum", wum);
        createVille("VNDP", "Ndop", ndop);

        createQuartier("QNKW", "Nkwen", bamendaVille);
        createQuartier("QUP", "Up Station", bamendaVille);
        createQuartier("QOT", "Old Town", bamendaVille);
        createQuartier("QCA", "Commercial Avenue", bamendaVille);
        createQuartier("QM4", "Mile 4 Nkwen", bamendaVille);
        createQuartier("QKBC", "Centre Kumbo", kumboVille);
    }

    // ========================= OUEST =========================
    private void seedOuest() {
        Region ouest = createRegion("OU", "Ouest");

        Departement bamboutos = createDepartement("BMB", "Bamboutos", ouest);
        Departement haut_nkam = createDepartement("HNK", "Haut-Nkam", ouest);
        Departement hauts_plateaux = createDepartement("HPL", "Hauts-Plateaux", ouest);
        Departement koung_khi = createDepartement("KKH", "Koung-Khi", ouest);
        Departement menoua = createDepartement("MNA", "Menoua", ouest);
        Departement mifi = createDepartement("MFI", "Mifi", ouest);
        Departement nde = createDepartement("NDE", "Nde", ouest);
        Departement noun = createDepartement("NON", "Noun", ouest);

        Arrondissement bafoussam1 = createArrondissement("BFS1", "Bafoussam 1er", mifi);
        createArrondissement("BFS2", "Bafoussam 2eme", mifi);
        createArrondissement("BFS3", "Bafoussam 3eme", mifi);
        Arrondissement dschang = createArrondissement("DSC", "Dschang", menoua);
        Arrondissement mbouda = createArrondissement("MBD", "Mbouda", bamboutos);
        Arrondissement bangangte = createArrondissement("BGT", "Bangangte", nde);
        Arrondissement foumban = createArrondissement("FMB", "Foumban", noun);
        createArrondissement("FMT", "Foumbot", noun);
        Arrondissement bafang = createArrondissement("BFG", "Bafang", haut_nkam);
        Arrondissement bandjoun = createArrondissement("BDJ", "Bandjoun", koung_khi);
        createArrondissement("BHM", "Baham", hauts_plateaux);

        Ville bafoussamVille = createVille("VBFS", "Bafoussam", bafoussam1);
        Ville dschangVille = createVille("VDSC", "Dschang", dschang);
        createVille("VMBD", "Mbouda", mbouda);
        Ville foumbanVille = createVille("VFMB", "Foumban", foumban);
        createVille("VBGT", "Bangangte", bangangte);
        createVille("VBFG", "Bafang", bafang);
        createVille("VBDJ", "Bandjoun", bandjoun);

        // Quartiers de Bafoussam (utilises pour les ecoles)
        createQuartier("QTD", "Tamdja", bafoussamVille);
        createQuartier("QDJ", "Djeleng", bafoussamVille);
        createQuartier("QKP", "Kamkop", bafoussamVille);
        createQuartier("QKG", "King-Place", bafoussamVille);
        createQuartier("QTG", "Tougang", bafoussamVille);
        createQuartier("QDSC", "Centre Dschang", dschangVille);
        createQuartier("QFMC", "Centre Foumban", foumbanVille);
    }

    // ========================= SUD =========================
    private void seedSud() {
        Region sud = createRegion("SU", "Sud");

        Departement dja_et_lobo = createDepartement("DJL", "Dja-et-Lobo", sud);
        Departement mvila = createDepartement("MVL", "Mvila", sud);
        Departement ocean = createDepartement("OCN", "Ocean", sud);
        Departement vallee_du_ntem = createDepartement("VDN", "Vallee-du-Ntem", sud);

        Arrondissement ebolowa1 = createArrondissement("EBW1", "Ebolowa 1er", mvila);
        createArrondissement("EBW2", "Ebolowa 2eme", mvila);
        Arrondissement kribi1 = createArrondissement("KRB1", "Kribi 1er", ocean);
        createArrondissement("KRB2", "Kribi 2eme", ocean);
        Arrondissement sangmelima = createArrondissement("SGM2", "Sangmelima", dja_et_lobo);
        Arrondissement ambam = createArrondissement("AMB", "Ambam", vallee_du_ntem);

        Ville ebolowaVille = createVille("VEBW", "Ebolowa", ebolowa1);
        Ville kribiVille = createVille("VKRB", "Kribi", kribi1);
        createVille("VSGM", "Sangmelima", sangmelima);
        createVille("VAMB", "Ambam", ambam);

        createQuartier("QEBC", "Centre Ebolowa", ebolowaVille);
        createQuartier("QNKO", "Nko'oveng", ebolowaVille);
        createQuartier("QKRC", "Centre Kribi", kribiVille);
    }

    // ========================= SUD-OUEST =========================
    private void seedSudOuest() {
        Region sud_ouest = createRegion("SW", "Sud-Ouest");

        Departement fako = createDepartement("FKO", "Fako", sud_ouest);
        Departement kupe_muanenguba = createDepartement("KPM", "Koupe-Manengouba", sud_ouest);
        createDepartement("LBL", "Lebialem", sud_ouest);
        Departement manyu = createDepartement("MNY", "Manyu", sud_ouest);
        Departement meme = createDepartement("MME", "Meme", sud_ouest);
        Departement ndian = createDepartement("NDN", "Ndian", sud_ouest);

        Arrondissement buea = createArrondissement("BUE", "Buea", fako);
        Arrondissement limbe1 = createArrondissement("LMB1", "Limbe 1er", fako);
        createArrondissement("LMB2", "Limbe 2eme", fako);
        Arrondissement tiko = createArrondissement("TKO", "Tiko", fako);
        Arrondissement kumba = createArrondissement("KBA", "Kumba", meme);
        Arrondissement mamfe = createArrondissement("MMF", "Mamfe", manyu);
        createArrondissement("BGM", "Bangem", kupe_muanenguba);
        createArrondissement("MDM", "Mundemba", ndian);

        Ville bueaVille = createVille("VBUE", "Buea", buea);
        Ville limbeVille = createVille("VLMB", "Limbe", limbe1);
        Ville kumbaVille = createVille("VKBA", "Kumba", kumba);
        createVille("VMMF", "Mamfe", mamfe);
        createVille("VTKO", "Tiko", tiko);

        createQuartier("QML", "Molyko", bueaVille);
        createQuartier("QGS", "Great Soppo", bueaVille);
        createQuartier("QBT", "Buea Town", bueaVille);
        createQuartier("QBD", "Bonduma", bueaVille);
        createQuartier("QLDB", "Down Beach", limbeVille);
        createQuartier("QLBC", "Centre Limbe", limbeVille);
        createQuartier("QKBAC", "Centre Kumba", kumbaVille);
    }

    // ========================= HELPERS =========================

    private void loadExistingQuartiers() {
        quartierRepository.findAll().forEach(q -> quartiersMap.put(q.getCode(), q));
    }

    private Region createRegion(String code, String nom) {
        Region r = new Region();
        r.setCode(code);
        r.setNom(nom);
        return regionRepository.save(r);
    }

    private Departement createDepartement(String code, String nom, Region region) {
        Departement d = new Departement();
        d.setCode(code);
        d.setNom(nom);
        d.setRegion(region);
        return departementRepository.save(d);
    }

    private Arrondissement createArrondissement(String code, String nom, Departement departement) {
        Arrondissement a = new Arrondissement();
        a.setCode(code);
        a.setNom(nom);
        a.setDepartement(departement);
        return arrondissementRepository.save(a);
    }

    private Ville createVille(String code, String nom, Arrondissement arrondissement) {
        Ville v = new Ville();
        v.setCode(code);
        v.setNom(nom);
        v.setArrondissement(arrondissement);
        return villeRepository.save(v);
    }

    private Quartier createQuartier(String code, String nom, Ville ville) {
        Quartier q = new Quartier();
        q.setCode(code);
        q.setNom(nom);
        q.setVille(ville);
        Quartier saved = quartierRepository.save(q);
        quartiersMap.put(code, saved);
        return saved;
    }
}
