FROM nexus.mercator-ocean.fr:33330/conda/miniconda3:latest
# Labels to be updated at build time in Jenkins
LABEL usage=miniconda
LABEL version=<version>
LABEL date=<date>
# Make RUN commands use `bash --login`:
RUN mkdir /root/.pip
COPY pip.conf /root/.pip/ 
COPY .condarc /root/
# Set up conda
RUN conda update -y --prefix /usr/local conda
# Initialize conda in bash config files:
RUN conda init bash
# Install bump2version in the base environment
RUN conda install -y bump2version setuptools
# Install mamba in the base environment
RUN conda install -y mamba -c conda-forge
# Create the test environment and install packages used in testing
SHELL ["/bin/bash", "-c"]
RUN mamba create -y --name test flake8 pytest pytest-cov coverage
RUN mamba create -y --name sphinx sphinx pydata-sphinx-theme dask-sphinx-theme sphinx_rtd_theme astropy-sphinx-theme sphinx-book-theme sphinx-material sphinx-tabs sphinxcontrib-programoutput jupyter_sphinx sphinx-nbexamples sphinx-tabs sphinx-argparse
# Install additional packages 
RUN apt-get update && apt-get -y install git wget unzip openjdk-8-jre-headless curl
# Install dependency-check
RUN mkdir dependency-check
RUN wget -O dependency-check/dependency-check.zip https://github.com/jeremylong/DependencyCheck/releases/download/v6.2.2/dependency-check-6.2.2-release.zip
RUN unzip -n dependency-check/dependency-check.zip
