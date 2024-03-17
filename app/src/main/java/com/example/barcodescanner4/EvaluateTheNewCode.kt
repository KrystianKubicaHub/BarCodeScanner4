package com.example.barcodescanner4

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.text.isDigitsOnly

class EvaluateTheNewCode(){
    private var code: Long? = null


    fun barcodeIsOk(barcode : String): String{
        // if function returned null, than code is ok
        if(barcode.length != 13){
            return "The length of the barcode must be 13"
        }
        if(!barcode.isDigitsOnly()){
            return "Barcode must consist only of numbers"
        }
        code = barcode.toLong()
        return ""

    }

    fun check_if_code_in_registry(): Boolean{
        return if( code in RAM_Database.list_of_barcodes){
            true
        }else{
            false
        }

    }

    fun getDataFromBarcode(new_opinion: OpinionOnThrowingAway? = null,
                           list_of_old_opinions: MutableList<OpinionOnThrowingAway>? = null,
                           required_washing: Boolean) : Barcode?{
        if(code.toString().length==13){

           val first_three_letters = code.toString().subSequence(0,3)





            val country = determineCountryOfOrgin(first_three_letters.toString().toInt())
            val manufacturer = ""
            val productName = ""


            var current_list_of_opinions = mutableListOf<OpinionOnThrowingAway>()

            if(new_opinion != null && list_of_old_opinions != null){
                current_list_of_opinions = list_of_old_opinions
                current_list_of_opinions.add(new_opinion)
            }else if(new_opinion != null){
                current_list_of_opinions = mutableListOf<OpinionOnThrowingAway>(new_opinion)
            }else if(list_of_old_opinions !== null){
                current_list_of_opinions = list_of_old_opinions
            }else{
                current_list_of_opinions = mutableListOf<OpinionOnThrowingAway>()
            }

            if(code != null){
                return Barcode(
                    code = code!!,
                    country_of_origin = country,
                    manufacturer = manufacturer,
                    product_name = productName,
                    opinion_on_throwing_away = current_list_of_opinions,
                    requiresWashing = required_washing)
            }

        }
        return null
    }

    private fun determineCountryOfOrgin(trinity: Int): String {
        val not_determined = "Kod kreskowy nie determinuje pochodzenia"
        val international_company ="Firma Międzynarodowa"
        val country_unknown = "Kraj nieznany"
        when(trinity){
            in 1..19 -> return "USA"
            in 20..29 -> return not_determined
            in 30..39 -> return "USA"
            in 40..49 -> return not_determined
            in 50..59 -> return "USA"
            in 60..99 -> return "USA"
            in 100..139 -> return "USA"
            in 200..299 -> return not_determined
            in 300..379 -> return "Francja"
            380 -> return "Bułgaria"
            383 -> return "Słowenia"
            385 -> return "Chorwacja"
            387 -> return "Bośnia-Hercegowina"
            389 -> return "Czarnogóra"
            in 400..440 -> return "Niemcy"
            in 450..459 -> return "Japonia"
            in 490..499 -> return "Japonia"
            in 460..469 -> return "Rosja"
            470 -> return "Kirgistan"
            471 -> return "Tajwan"
            474 -> return "Estonia"
            475 -> return "Łotwa"
            476 -> return "Azerbejdżan"
            477 -> return "Litwa"
            478 -> return "Uzbekistan"
            479 -> return "Sri Lanka"
            480 -> return "Filipiny"
            481 -> return "Białoruś"
            482 -> return "Ukraina"
            483 -> return "Turkmenistan"
            484 -> return "Mułdawia"
            485 -> return "Armenia"
            486 -> return "Gruzja"
            487 -> return "Kazacchstan"
            488 -> return "Tadżykistan"
            489 -> return "HongKong"
            in 500..509 -> return "Wielka Brytania"
            520 -> return "Grecja"
            528 -> return "Liban"
            529 -> return "Cypr"
            530 -> return "Albania"
            531 -> return "Macedonia Północna"
            535 -> return "Malta"
            539 -> return "Irlandia"
            in 540..549 -> return "Belgia/Luksemburg"
            560 -> return "Portugalia"
            569 -> return "Islandia"
            in 570..579 -> return "Dania/Wyspy Owccze/Grenlandia"
            590 -> return "Polska"
            594 -> return "Rumunia"
            599 -> return "Węgry"
            in 600..601 -> return "Afryka Południowa"
            603 -> return "Ghana"
            604 -> return "Senegal"
            608 -> return "Bahrajn"
            609 -> return "Mauritius"
            611 -> return "Maroko"
            613 -> return "Algieria"
            615 -> return "Nigeria"
            616 -> return "Kenia"
            617 -> return "Kamerun"
            618 -> return " Wybrzeże Kości Słoniowej"
            619 -> return "Tunezja"
            620 -> return "Tanzania"
            621 -> return "Syria"
            622 -> return "Egipt"
            623 -> return "Brunei"
            624 -> return "Libia"
            625 -> return "Jordania"
            626 -> return "Iran"
            627 -> return "Kuwejt"
            628 -> return "Arabia Saudyjska"
            629 -> return "Zjednoczone Emiraty Arabskie"
            in 640..649 -> return "Finlandia"
            in 690..699 -> return "Chińska Republika Ludowa"
            in 700..709 -> return "Norwegia"
            729 -> return "Izrael"
            in 730..739 -> return "Szwecja"
            740 -> return "Gwatemala"
            741 -> return "Salwador"
            742 -> return "Honduras"
            743 -> return "Nikaragua"
            744 -> return "Kostaryka"
            745 -> return "Panama"
            746 -> return "Dominikana"
            750 -> return "Meksyk"
            754,755 -> return "Kanada"
            759 -> return "Wenezuela"
            in 760..769 -> return "Szwajcaria"
            770 -> return "Kolumbia"
            773 -> return "Urugwaj"
            775 -> return "Peru"
            777 -> return "Boliwia"
            779 -> return "Argentyna"
            780 -> return "Chile"
            784 -> return "Paragwaj"
            786 -> return "Ekwador"
            789,790 -> return "Brazylia"
            in 800..839 -> return "Włochy"
            in 840..849 -> return "Hiszpania"
            850 -> return "Kuba"
            858 -> return "Słowacja"
            859 -> return "Czechy"
            860 -> return "Serbia"
            865 -> return "Mongolia"
            867 -> return "Korea Północna"
            868,869 -> return "Turcja"
            in 870..879 -> return "Niderlandy"
            880 -> return "Korea Południowa"
            883 -> return "Mjanma (Birma)"
            884 -> return "Kambodża"
            885 -> return "Tajlandia"
            888 -> return "Singapur"
            890 -> return "Indie"
            893 -> return "Wietnam"
            896 -> return "Pakistan"
            899 -> return "Indonezja"
            in 900..919 -> return "Austria"
            in 930..939 -> return "Australia"
            in 940..949 -> return "Nowa Zelandia"
            950 -> return international_company
            951 -> return international_company
            955 -> return "Malezja"
            958  -> return "Makau"
            in 960..982 -> return international_company
            in 990..999 -> return "Zależne od kraju występowania"
            else -> return country_unknown
        }
    }

    fun getBarcodeFromRAM(): Barcode?{

        for(i in RAM_Database.barcodes_data){
            if(i.code == code){
                return i
            }
        }
        return null
    }
}