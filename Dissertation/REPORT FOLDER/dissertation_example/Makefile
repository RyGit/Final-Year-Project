main: main.tex project.bib
	pdflatex main
	bibtex main
	pdflatex main
	pdflatex main

main_dvips: main.tex project.bib
	latex main
	bibtex main
	latex main
	latex main
	dvips -G0 -Ppdf -o main.ps main.dvi
	ps2pdf -dCompatibilityLevel=1.3 -dMaxSubsetPct=100 -dSubsetFonts=true -dEmbedAllFonts=true -dAutoFilterColorImages=false -dAutoFilterGrayImages=false -dColorImageFilter=/FlateEncode -dGrayImageFilter=/FlateEncode -dMonoImageFilter=/FlateEncode main.ps main.pdf

clean:
	for d in . ANALYSIS CODE CONCLUSIONS DEVELOPMENT EVALUATION FRONT INTRODUCTION SYNTHESIS TOR; do \
	    (cd $$d; rm -f *~ *.aux *.bbl *.blg *.dvi *.lof *.log *.lot *.out *.pdf *.ps *.toc) \
	done
