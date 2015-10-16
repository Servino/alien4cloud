package alien4cloud.it.orchestrators;

import org.junit.Assert;

import alien4cloud.it.Context;
import alien4cloud.model.orchestrators.locations.LocationResourceTemplate;
import alien4cloud.rest.model.RestResponse;
import alien4cloud.rest.orchestrator.model.CreateLocationResourceTemplateRequest;
import alien4cloud.rest.orchestrator.model.LocationDTO;
import alien4cloud.rest.orchestrator.model.UpdateLocationResourceTemplatePropertyRequest;
import alien4cloud.rest.utils.JsonUtil;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OrchestrationLocationResourceSteps {

    @When("^I create a resource of type \"([^\"]*)\" named \"([^\"]*)\" related to the location \"([^\"]*)\"/\"([^\"]*)\"$")
    public void I_create_a_resource_of_type_named_related_to_the_location_(String resourceType, String resourceName, String orchestratorName, String locationName)
            throws Throwable {
        String orchestratorId = Context.getInstance().getOrchestratorId(orchestratorName);
        String locationId = Context.getInstance().getOrchestratorLocation(orchestratorId, locationName);
        String restUrl = String.format("/rest/orchestrators/%s/locations/%s/resources", orchestratorId, locationId);
        CreateLocationResourceTemplateRequest request = new CreateLocationResourceTemplateRequest();
        request.setResourceName(resourceName);
        request.setResourceType(resourceType);
        String resp = Context.getRestClientInstance().postJSon(restUrl, JsonUtil.toString(request));

        RestResponse<LocationResourceTemplate> response = JsonUtil.read(resp, LocationResourceTemplate.class);
        Context.getInstance().registerOrchestratorLocationResource(orchestratorId, locationId, response.getData().getId(), resourceName);
        Context.getInstance().registerRestResponse(resp);
    }

    @When("^I get the location \"([^\"]*)\"/\"([^\"]*)\"$")
    public void I_get_the_location_(String orchestratorName, String locationName) throws Throwable {
        String orchestratorId = Context.getInstance().getOrchestratorId(orchestratorName);
        String locationId = Context.getInstance().getOrchestratorLocation(orchestratorId, locationName);
        String restUrl = String.format("/rest/orchestrators/%s/locations/%s", orchestratorId, locationId);
        String resp = Context.getRestClientInstance().get(restUrl);
        Context.getInstance().registerRestResponse(resp);
    }

    @Then("^The location should contains a resource with name \"([^\"]*)\" and type \"([^\"]*)\"$")
    public void The_location_should_contains_a_resource_with_name_and_type(String resourceName, String resourceType) throws Throwable {
        String restResponse = Context.getInstance().getRestResponse();
        RestResponse<LocationDTO> response = JsonUtil.read(restResponse, LocationDTO.class);
        LocationDTO locationDTO = response.getData();
        boolean found = false;
        for (LocationResourceTemplate lrt : locationDTO.getResources().getConfigurationTemplates()) {
            if (lrt.getName().equals(resourceName) && lrt.getTypes().contains(resourceType)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found);
    }

    @When("^I update the property \"([^\"]*)\" to \"([^\"]*)\" for the resource named \"([^\"]*)\" related to the location \"([^\"]*)\"/\"([^\"]*)\"$")
    public void I_update_the_property_to_for_the_resource_named_related_to_the_location_(String propertyName, String propertyValue, String resourceName,
            String orchestratorName, String locationName)
            throws Throwable {
        String orchestratorId = Context.getInstance().getOrchestratorId(orchestratorName);
        String locationId = Context.getInstance().getOrchestratorLocation(orchestratorId, locationName);
        String resourceId = Context.getInstance().getOrchestratorLocationResource(orchestratorId, locationId, resourceName);
        String restUrl = String.format("/rest/orchestrators/%s/locations/%s/resources/%s/template/properties", orchestratorId, locationId, resourceId);
        UpdateLocationResourceTemplatePropertyRequest request = new UpdateLocationResourceTemplatePropertyRequest();
        request.setPropertyName(propertyName);
        request.setPropertyValue(propertyValue);
        String resp = Context.getRestClientInstance().postJSon(restUrl, JsonUtil.toString(request));
        Context.getInstance().registerRestResponse(resp);
    }

    @When("^I update the capability \"([^\"]*)\" property \"([^\"]*)\" to \"([^\"]*)\" for the resource named \"([^\"]*)\" related to the location \"([^\"]*)\"/\"([^\"]*)\"$")
    public void I_update_the_capability_property_to_for_the_resource_named_related_to_the_location_(String capabilityName, String propertyName,
            String propertyValue, String resourceName, String orchestratorName, String locationName) throws Throwable {
        String orchestratorId = Context.getInstance().getOrchestratorId(orchestratorName);
        String locationId = Context.getInstance().getOrchestratorLocation(orchestratorId, locationName);
        String resourceId = Context.getInstance().getOrchestratorLocationResource(orchestratorId, locationId, resourceName);
        String restUrl = String.format("/rest/orchestrators/%s/locations/%s/resources/%s/template/capabilities/%s/properties", orchestratorId, locationId,
                resourceId, capabilityName);
        UpdateLocationResourceTemplatePropertyRequest request = new UpdateLocationResourceTemplatePropertyRequest();
        request.setPropertyName(propertyName);
        request.setPropertyValue(propertyValue);
        String resp = Context.getRestClientInstance().postJSon(restUrl, JsonUtil.toString(request));
        Context.getInstance().registerRestResponse(resp);
    }

    @When("^I autogenerate the on-demand resources for the location \"([^\"]*)\"/\"([^\"]*)\"$")
    public void I_autogenerate_the_on_demand_resources_for_the_location_(String orchestratorName, String locationName) throws Throwable {
        String orchestratorId = Context.getInstance().getOrchestratorId(orchestratorName);
        String locationId = Context.getInstance().getOrchestratorLocation(orchestratorId, locationName);
        String restUrl = String.format("/rest/orchestrators/%s/locations/%s/resources/auto-configure", orchestratorId, locationId);
        String resp = Context.getRestClientInstance().get(restUrl);
        Context.getInstance().registerRestResponse(resp);
    }

}
