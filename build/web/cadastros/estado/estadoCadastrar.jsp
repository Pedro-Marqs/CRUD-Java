<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="iso-8859-1"%>
<jsp:include page="/header.jsp"/>
<jsp:include page="/menu.jsp"/>

<div class="container-fluid">
    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800">Estados</h1>
    <p class="mb-4">Formulário de Cadastro</p>

    <a class="btn btn-secondary mb-4" href="${pageContext.request.contextPath}/EstadoListar">
        <i class="fas fa-undo-alt"></i>
        <strong>Voltar</strong>
    </a>
    <div class="row">
        <!-- Campos de cadastro -->        
        <div class="col-lg-9">
            <div class="card shadow mb-4">
                <div class="card-body">
                    <div class="form-group">
                        <label>Id</label>
                        <input class="form-control" type="text" name="id" id="id" 
                               value="${estado.id}" readonly="readonly"/>
                    </div>
                    <div class="form-group">
                        <label>Nome</label>
                        <input class="form-control" type="text" name="nomeEstado" id="nomeEstado" 
                               value="${estado.nomeEstado}" size="100" maxlength="100"/>
                    </div>
                    <div class="form-group">
                        <label>Sigla</label>
                        <input class="form-control" type="text" name="siglaEstado" id="siglaEstado" 
                               value="${estado.siglaEstado}" size="11" maxlength="11"/>
                    </div>
                    <!-- Botão de Confirmação --> 
                    <div class="form-group">
                        <button class="btn btn-success" type="submit" id="submit" onclick="validarCampos()">
                            Salvar Documento</button>
                    </div> 
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        console.log("entrei na ready do documento");

        $('#siglaEstado').blur(function () {
            console.log("verificando siglaEstado no backend");

            // Captura o valor do campo no momento do evento
            let siglaEstado = $('#siglaEstado').val().trim();

            if (siglaEstado == '') {
                return; // evita chamadas vazias
            }

            if ($('#id').val() == 0) {
                // Faz a verificação no backend
                $.ajax({
                    type: 'get',
                    url: 'EstadoVerificarSigla',
                    data: {siglaEstado: siglaEstado},
                    success: function (response) {
                        console.log("resposta validacao backend:");
                        console.log(response);
                        if (response == '1') {
                            // Desativa temporariamente o blur para evitar loop
                            $('#siglaEstado').off('blur');

                            Swal.fire({
                                position: 'center',
                                icon: 'warning',
                                title: 'Estado já cadastrado!',
                                text: 'Por favor, verifique o estado informado.',
                                showConfirmButton: true,
                                timer: 4000
                            }).then(function () {
                                $('#nomeEstado').focus();

                                // Reativa o blur após foco ser alterado
                                setTimeout(() => {
                                    $('#siglaEstado').on('blur', verificarSigla);
                                }, 100);
                            });
                        }
                    },
                    error: function () {
                        console.log("Erro ao verificar siglaEstado no servidor.");
                    }
                });
            }
        });

        $('#nome').focus();
    });


    function validarCampos() {
        console.log("entrei na validação de campos");
        if (document.getElementById("nomeEstado").value == '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique o nome do estado!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#nomeEstado").focus();
        } else if (document.getElementById("siglaEstado").value == '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique a sigla do estado!',
                showConfirmButton: false,
                timer: 1000
            });
            $("#siglaEstado").focus();
        } else {
            gravarDados();
        }
    }

    function gravarDados() {
        console.log("Gravando dados ....");
        $.ajax({
            type: 'post',
            url: 'EstadoCadastrar',
            data: {
                id: $('#id').val(),
                nomeEstado: $('#nomeEstado').val().toUpperCase(),
                siglaEstado: $('#siglaEstado').val().toUpperCase()
            },
            success: function (data) {
                console.log("resposta servlet->");
                console.log(data);
                if (data == 1) {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'Sucesso',
                        text: 'Estado gravado com sucesso!',
                        showConfirmButton: true,
                        timer: 3000
                    }).then(function () {
                        // RECARREGA A PÁGINA INTEIRA com formulário limpo
                        window.location.href = 'EstadoNovo';
                    });
                } else if (data == 2) {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'Sigla já cadastrada!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomeEstado').focus();
                        }, 50); // um pequeno atraso resolve o problema
                    });
                } else {
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: 'Não foi possível gravar o estado!',
                        showConfirmButton: true,
                        timer: 5000
                    }).then(function () {
                        setTimeout(function () {
                            $('#nomeEstado').focus();
                        }, 50); // um pequeno atraso resolve o problema
                    });
                }
            },
            error: function () {
                Swal.fire({
                    position: 'center',
                    icon: 'error',
                    title: 'Erro de comunicação',
                    text: 'Falha na comunicação com o servidor.',
                    showConfirmButton: true,
                    timer: 5000
                }).then(function () {
                    setTimeout(function () {
                        $('#nomeEstado').focus();
                    }, 50);
                });
            }
        });
    }


</script>   
<jsp:include page="/footer.jsp"/>