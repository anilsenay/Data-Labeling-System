import pandas as pd
import numpy as np
pollReader = pd.read_excel('./Data/CES3063_Fall2020_rptSinifListesi.xls.xlsx')
# or using sheet index starting 0
pd.set_option('display.max_rows', 500)
pd.set_option('display.max_columns', 500)
pd.set_option('display.width', 20000)
QuestionCounter = 0
AnswerCounter = 0


for i in range(len(pollReader
.columns)):
    if (i >= 4):
        if(i % 2 == 0):
            QuestionCounter += 1
            pollReader
            .rename( columns={"Unnamed: "+str(i) :"Question"+str(QuestionCounter-1)}, inplace=True )
        else:
            AnswerCounter += 1
            pollReader
            .rename( columns={"Unnamed: "+str(i) :"Answer"+str(AnswerCounter-1)}, inplace=True )


pollReader = pollReader.replace(np.nan, "", regex=True)
print(pollReader.loc[::])